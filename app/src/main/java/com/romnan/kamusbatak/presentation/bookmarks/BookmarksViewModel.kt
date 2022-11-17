package com.romnan.kamusbatak.presentation.bookmarks

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romnan.kamusbatak.domain.model.Language
import com.romnan.kamusbatak.domain.repository.DictionaryRepository
import com.romnan.kamusbatak.domain.util.Resource
import com.romnan.kamusbatak.presentation.util.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val dictionaryRepository: DictionaryRepository,
) : ViewModel() {

    private val _state = mutableStateOf(BookmarksScreenState.defaultValue)
    val state: State<BookmarksScreenState> = _state

    private val _eventFlow = Channel<UIEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    init {
        getBookmarkedEntries()
    }

    private var getBookmarkedEntriesJob: Job? = null
    fun getBookmarkedEntries() {
        getBookmarkedEntriesJob?.cancel()
        getBookmarkedEntriesJob = viewModelScope.launch {
            dictionaryRepository.getBookmarkedEntries(Language.IND).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
                        _state.value = state.value.copy(isLoadingIndEntries = false)
                    }
                    is Resource.Loading -> {
                        _state.value = state.value.copy(isLoadingIndEntries = true)
                    }
                    is Resource.Success -> {
                        _state.value = state.value.copy(
                            isLoadingIndEntries = false,
                            indEntries = result.data ?: state.value.indEntries,
                        )
                    }
                }
            }.launchIn(this)

            dictionaryRepository.getBookmarkedEntries(Language.BTK).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
                        _state.value = state.value.copy(isLoadingBtkEntries = false)
                    }
                    is Resource.Loading -> {
                        _state.value = state.value.copy(isLoadingBtkEntries = true)
                    }
                    is Resource.Success -> {
                        _state.value = state.value.copy(
                            isLoadingBtkEntries = false,
                            btkEntries = result.data ?: state.value.indEntries,
                        )
                    }
                }
            }.launchIn(this)
        }
    }
}