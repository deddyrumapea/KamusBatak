package com.romnan.kamusbatak.presentation.preferences

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romnan.kamusbatak.R
import com.romnan.kamusbatak.domain.repository.OfflineSupportRepository
import com.romnan.kamusbatak.presentation.util.UIEvent
import com.romnan.kamusbatak.domain.util.Resource
import com.romnan.kamusbatak.domain.util.UIText
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
    private val offlineSupportRepository: OfflineSupportRepository
) : ViewModel() {

    private val _state = mutableStateOf(OfflineSupportScreenState.defaultValue)
    val state: State<OfflineSupportScreenState> = _state

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var downloadUpdateJob: Job? = null
    private var getLastUpdatedJob: Job? = null

    init {
        getLastUpdated()
    }

    fun onEvent(event: PreferencesEvent) {
        when (event) {
            is PreferencesEvent.DownloadUpdate -> downloadUpdate()
        }
    }


    private fun getLastUpdated() {
        getLastUpdatedJob?.cancel()
        getLastUpdatedJob = viewModelScope.launch {
            offlineSupportRepository.lastUpdatedAt.onEach { timeMillis ->
                _state.value = state.value.copy(lastUpdatedTimeMillis = timeMillis)
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