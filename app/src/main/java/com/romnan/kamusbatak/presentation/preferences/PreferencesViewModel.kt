package com.romnan.kamusbatak.presentation.preferences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romnan.kamusbatak.R
import com.romnan.kamusbatak.domain.model.ThemeMode
import com.romnan.kamusbatak.domain.repository.DictionaryRepository
import com.romnan.kamusbatak.domain.repository.PreferencesRepository
import com.romnan.kamusbatak.domain.util.Resource
import com.romnan.kamusbatak.domain.util.UIText
import com.romnan.kamusbatak.presentation.util.UIEvent
import com.zhuinden.flowcombinetuplekt.combineTuple
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val dictionaryRepository: DictionaryRepository,
    private val preferencesRepository: PreferencesRepository,
) : ViewModel() {

    private val isUpdatingLocalDb = MutableStateFlow(false)
    private val localDbLastUpdatedAt = dictionaryRepository.localDbLastUpdatedAt
    private val currentThemeMode = preferencesRepository.themeMode
    private val isThemeModeDialogVisible = MutableStateFlow(false)
    private val visiblePermissionDialogQueue = MutableStateFlow<List<String>>(emptyList())
    private val isAppRatingDialogVisible = MutableStateFlow(false)

    val state: StateFlow<PreferencesScreenState> = combineTuple(
        isUpdatingLocalDb,
        localDbLastUpdatedAt,
        currentThemeMode,
        isThemeModeDialogVisible,
        visiblePermissionDialogQueue,
        isAppRatingDialogVisible,
    ).map { (
                isUpdatingLocalDb,
                localDbLastUpdatedAt,
                currentThemeMode,
                isThemeModeDialogVisible,
                visiblePermissionDialogQueue,
                isAppRatingDialogVisible,
            ) ->
        PreferencesScreenState(
            isUpdatingLocalDb = isUpdatingLocalDb,
            localDbLastUpdatedAt = localDbLastUpdatedAt,
            currentThemeMode = currentThemeMode,
            isThemeModeDialogVisible = isThemeModeDialogVisible,
            visiblePermissionDialogQueue = visiblePermissionDialogQueue,
            isAppRatingDialogVisible = isAppRatingDialogVisible,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = PreferencesScreenState(),
    )

    val dailyWordSettings: StateFlow<DailyWordSettingsPresentation> =
        preferencesRepository.dailyWordTime.map { DailyWordSettingsPresentation(it) }.stateIn(
            viewModelScope, SharingStarted.Lazily, DailyWordSettingsPresentation.defaultValue
        )

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var onUpdateLocalDbJob: Job? = null
    fun onUpdateLocalDb() {
        onUpdateLocalDbJob?.cancel()
        onUpdateLocalDbJob = viewModelScope.launch {
            dictionaryRepository.updateLocalDb().onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        isUpdatingLocalDb.update { false }
                    }

                    is Resource.Loading -> {
                        isUpdatingLocalDb.update { true }
                    }

                    is Resource.Error -> {
                        isUpdatingLocalDb.update { false }
                        _eventFlow.emit(
                            UIEvent.ShowSnackbar(
                                result.uiText ?: UIText.StringResource(R.string.em_unknown)
                            )
                        )
                    }
                }
            }.launchIn(this)
        }
    }

    private var onThemeModeChosenJob: Job? = null
    fun onThemeModeChosen(themeMode: ThemeMode) {
        onThemeModeChosenJob?.cancel()
        onThemeModeDialogVisibilityChange(visible = false)
        onThemeModeChosenJob = viewModelScope.launch {
            preferencesRepository.setThemeMode(themeMode)
        }
    }

    fun onThemeModeDialogVisibilityChange(visible: Boolean) {
        isThemeModeDialogVisible.update { visible }
    }

    private var onDailyWordTimePickedJob: Job? = null
    fun onDailyWordTimePicked(hourOfDay: Int, minute: Int) {
        onDailyWordTimePickedJob?.cancel()
        onDailyWordTimePickedJob = viewModelScope.launch {
            val timeInMillis = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hourOfDay)
                set(Calendar.MINUTE, minute)
            }.timeInMillis

            preferencesRepository.setDailyWordTime(timeInMillis = timeInMillis)
        }
    }

    private var onTurnOffDailyWordJob: Job? = null
    fun onTurnOffDailyWord() {
        onTurnOffDailyWordJob?.cancel()
        onTurnOffDailyWordJob = viewModelScope.launch {
            preferencesRepository.setDailyWordTime(timeInMillis = null)
        }
    }

    private var onClickAppVersionJob: Job? = null
    fun onClickAppVersion() {
        onClickAppVersionJob?.cancel()
        onClickAppVersionJob = viewModelScope.launch {
            _eventFlow.emit(UIEvent.ShowSnackbar(UIText.StringResource(R.string.about)))
        }
    }

    fun onPermissionResult(permission: String, isGranted: Boolean) {
        if (!isGranted && !state.value.visiblePermissionDialogQueue.contains(permission)) {
            visiblePermissionDialogQueue.update { it + permission }
        }
    }

    fun onDismissPermissionDialog() {
        visiblePermissionDialogQueue.update {
            it.toMutableList().apply { removeFirst() }
        }
    }

    fun onClickRateApp() {
        isAppRatingDialogVisible.update { true }
    }

    fun onDismissRateAppDialog() {
        isAppRatingDialogVisible.update { false }
    }
}