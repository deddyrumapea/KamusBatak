package com.romnan.kamusbatak.feature_entries_finder.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romnan.kamusbatak.core.domain.repository.PreferencesRepository
import com.romnan.kamusbatak.core.util.Resource
import com.romnan.kamusbatak.feature_entries_finder.domain.repository.OfflineSupportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfflineSupportViewModel @Inject constructor(
    private val repository: OfflineSupportRepository,
    private val prefRepository: PreferencesRepository
) : ViewModel() {

    private val _state = mutableStateOf(OfflineSupportState())
    val state: State<OfflineSupportState> = _state

    private var downloadUpdateJob: Job? = null
    private var getLastUpdatedJob: Job? = null

    init {
        downloadUpdate()
        getLastUpdated()
    }

    fun onEvent(event: OfflineSupportEvent) {
        when (event) {
            is OfflineSupportEvent.DownloadUpdate -> {
                downloadUpdate()
                getLastUpdated()
            }
        }
    }


    private fun getLastUpdated() {
        getLastUpdatedJob?.cancel()
        getLastUpdatedJob = viewModelScope.launch {
            prefRepository.getLastOfflineSupportUpdatedAt().onEach {
                _state.value = state.value.copy(lastUpdated = it)
            }.launchIn(this)
        }
    }


    private fun downloadUpdate() {
        downloadUpdateJob?.cancel()
        downloadUpdateJob = viewModelScope.launch {
            repository.downloadUpdate().onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.value = state.value.copy(
                            isUpdating = false,
                            isErrorUpdating = false,
                            isUpToDate = true
                        )
                    }
                    is Resource.Error -> {
                        _state.value = state.value.copy(
                            isUpdating = false,
                            isErrorUpdating = true,
                            isUpToDate = false
                        )
                    }
                    is Resource.Loading -> {
                        _state.value = state.value.copy(
                            isUpdating = true,
                            isErrorUpdating = false,
                            isUpToDate = false
                        )
                    }
                }
            }.launchIn(this)
        }
    }

}