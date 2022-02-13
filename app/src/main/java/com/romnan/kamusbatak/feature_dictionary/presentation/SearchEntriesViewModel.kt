package com.romnan.kamusbatak.feature_dictionary.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romnan.kamusbatak.core.util.Resource
import com.romnan.kamusbatak.feature_dictionary.domain.use_case.SearchEntries
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
class SearchEntriesViewModel @Inject constructor(
    private val searchEntries: SearchEntries
) : ViewModel() {

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    private val _state = mutableStateOf(SearchEntriesState())
    val state: State<SearchEntriesState> = _state

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var searchJob: Job? = null

    fun onEvent(event: SearchEntriesEvent) {
        when (event) {
            is SearchEntriesEvent.QueryChange -> {
                _searchQuery.value = event.query
                fetchEntries()
            }

            is SearchEntriesEvent.LanguagesSwap -> {
                swapLanguage()
                fetchEntries()
            }
        }
    }

    private fun swapLanguage() {
        _state.value = _state.value.copy(
            sourceLanguage = _state.value.targetLanguage,
            targetLanguage = _state.value.sourceLanguage
        )
    }

    private fun clearEntries() {
        _state.value = _state.value.copy(
            entries = emptyList()
        )
    }

    private fun fetchEntries() {
        searchJob?.cancel()

        if (searchQuery.value.isBlank()) {
            clearEntries()
            return
        }

        searchJob = viewModelScope.launch {
            delay(500L)
            searchEntries(
                keyword = searchQuery.value,
                kwLanguage = state.value.sourceLanguage
            ).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.value = state.value.copy(
                            entries = result.data ?: emptyList(),
                            isLoading = false
                        )
                    }
                    is Resource.Loading -> {
                        _state.value = state.value.copy(
                            entries = result.data ?: emptyList(),
                            isLoading = true
                        )
                    }
                    is Resource.Error -> {
                        _state.value = state.value.copy(
                            entries = result.data ?: emptyList(),
                            isLoading = false
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