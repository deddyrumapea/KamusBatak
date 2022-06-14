package com.romnan.kamusbatak.presentation.preferences

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romnan.kamusbatak.R
import com.romnan.kamusbatak.domain.repository.DictionaryRepository
import com.romnan.kamusbatak.domain.util.Resource
import com.romnan.kamusbatak.domain.util.UIText
import com.romnan.kamusbatak.presentation.util.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val dictionaryRepository: DictionaryRepository,
) : ViewModel() {

    private val _state = mutableStateOf(PreferencesScreenState.defaultValue)
    val state: State<PreferencesScreenState> = _state

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        getLastUpdated()
    }

    private var getLastUpdatedJob: Job? = null
    private fun getLastUpdated() {
        getLastUpdatedJob?.cancel()
        getLastUpdatedJob = viewModelScope.launch {
            dictionaryRepository.localDbLastUpdatedAt.onEach { timeMillis ->
                _state.value = state.value.copy(lastUpdatedTimeMillis = timeMillis)
            }.launchIn(this)
        }
    }

    private var onUpdateLocalDbJob: Job? = null
    fun onUpdateLocalDb() {
        onUpdateLocalDbJob?.cancel()
        onUpdateLocalDbJob = viewModelScope.launch {
            dictionaryRepository.updateLocalDb().onEach { result ->
                when (result) {
                    is Resource.Success -> _state.value = state.value.copy(isUpdating = false)
                    is Resource.Loading -> _state.value = state.value.copy(isUpdating = true)
                    is Resource.Error -> {
                        _state.value = state.value.copy(isUpdating = false)
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
}