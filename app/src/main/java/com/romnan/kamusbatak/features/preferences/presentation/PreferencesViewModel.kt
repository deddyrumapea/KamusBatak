package com.romnan.kamusbatak.features.preferences.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romnan.kamusbatak.core.domain.repository.PreferencesRepository
import com.romnan.kamusbatak.core.util.Resource
import com.romnan.kamusbatak.features.entriesFinder.domain.repository.OfflineSupportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(
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
            prefRepository.getLastOfflineSupportUpdatedAt().onEach {
                it?.let {
                    val sdf = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
                    val date = Date()
                    date.time = it

                    _state.value = state.value.copy(lastUpdated = sdf.format(date))
                }
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