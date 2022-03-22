package com.romnan.kamusbatak.features.entriesFinder.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romnan.kamusbatak.core.util.Resource
import com.romnan.kamusbatak.features.entriesFinder.domain.repository.EntriesFinderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EntriesFinderViewModel @Inject constructor(
    private val repository: EntriesFinderRepository
) : ViewModel() {

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    private val _state = mutableStateOf(EntriesFinderState())
    val state: State<EntriesFinderState> = _state

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var searchJob: Job? = null

    fun onEvent(event: EntriesFinderEvent) {
        when (event) {
            is EntriesFinderEvent.QueryChange -> {
                _searchQuery.value = event.query
                fetchEntries()
            }

            is EntriesFinderEvent.LanguagesSwap -> {
                swapLanguage()
                fetchEntries()
            }

            is EntriesFinderEvent.SetShowUpdateDialog -> _state.value =
                state.value.copy(
                    isUpdateDialogShown = event.show
                )

            is EntriesFinderEvent.SetShowOptionsMenu -> _state.value = state.value.copy(
                isOptionsMenuShown = event.show
            )
        }
    }

    private fun swapLanguage() {
        _state.value = state.value.copy(
            sourceLanguage = state.value.targetLanguage,
            targetLanguage = state.value.sourceLanguage
        )
    }

    private fun clearEntries() {
        _state.value = state.value.copy(
            entries = emptyList()
        )
    }

    private fun fetchEntries() {
        if (searchQuery.value.isBlank()) {
            clearEntries()
            return
        }

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500L)
            repository.getEntries(
                keyword = searchQuery.value,
                srcLang = state.value.sourceLanguage
            ).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.value = state.value.copy(
                            entries = result.data ?: emptyList(),
                            isLoadingEntries = false
                        )
                    }
                    is Resource.Loading -> {
                        _state.value = state.value.copy(
                            entries = result.data ?: emptyList(),
                            isLoadingEntries = true
                        )
                    }
                    is Resource.Error -> {
                        _state.value = state.value.copy(
                            entries = result.data ?: emptyList(),
                            isLoadingEntries = false
                        )
                        // TODO: extract string resource
                        _eventFlow.emit(UIEvent.ShowSnackbar(result.message ?: "Unknown error"))
                    }
                }
            }.launchIn(this)
        }
    }

    sealed class UIEvent {
        data class ShowSnackbar(val message: String) : UIEvent()
    }
}