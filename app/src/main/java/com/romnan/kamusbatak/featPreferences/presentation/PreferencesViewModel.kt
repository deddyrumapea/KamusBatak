package com.romnan.kamusbatak.featPreferences.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romnan.kamusbatak.R
import com.romnan.kamusbatak.core.domain.repository.OfflineSupportRepository
import com.romnan.kamusbatak.core.presentation.util.UIEvent
import com.romnan.kamusbatak.core.util.Constants
import com.romnan.kamusbatak.core.util.Resource
import com.romnan.kamusbatak.core.util.UIText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val offlineSupportRepository: OfflineSupportRepository
) : ViewModel() {

    private val _state = mutableStateOf(OfflineSupportState())
    val state: State<OfflineSupportState> = _state

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var downloadUpdateJob: Job? = null
    private var getLastUpdatedJob: Job? = null

    init {
        downloadUpdate()
        getLastUpdated()
    }

    fun onEvent(event: PreferencesEvent) {
        when (event) {
            is PreferencesEvent.DownloadUpdate -> {
                downloadUpdate()
                getLastUpdated()
            }
        }
    }


    private fun getLastUpdated() {
        getLastUpdatedJob?.cancel()
        getLastUpdatedJob = viewModelScope.launch {
            offlineSupportRepository.getLastUpdatedAt().onEach { timeInMillis ->
                timeInMillis?.let {
                    val sdf = SimpleDateFormat(Constants.PATTERN_DATE, Locale.getDefault())
                    val date = Date().apply { this.time = timeInMillis }
                    _state.value = state.value.copy(lastUpdated = sdf.format(date))
                }
            }.launchIn(this)
        }
    }

    private fun downloadUpdate() {
        downloadUpdateJob?.cancel()
        downloadUpdateJob = viewModelScope.launch {
            offlineSupportRepository.downloadUpdate().onEach { result ->
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