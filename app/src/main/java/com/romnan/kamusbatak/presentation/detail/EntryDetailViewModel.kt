package com.romnan.kamusbatak.presentation.detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romnan.kamusbatak.presentation.util.UIEvent
import com.romnan.kamusbatak.domain.util.Resource
import com.romnan.kamusbatak.domain.repository.EntryDetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import logcat.logcat
import javax.inject.Inject

@HiltViewModel
class EntryDetailViewModel @Inject constructor(
    private val repository: EntryDetailRepository,
) : ViewModel() {
    private val _state = mutableStateOf(EntryDetailScreenState.defaultValue)
    val state: State<EntryDetailScreenState> = _state

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var setEntryIdJob: Job? = null
    fun setEntryId(entryId: Int?) {
        logcat { "setEntryId: $entryId" }
        if (entryId == null) return
        setEntryIdJob?.cancel()
        setEntryIdJob = viewModelScope.launch {
            repository.getEntry(id = entryId).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        _state.value = state.value.copy(isLoading = false)
                        result.uiText?.let { _eventFlow.emit(UIEvent.ShowSnackbar(it)) }
                    }

                    is Resource.Loading -> {
                        _state.value = state.value.copy(isLoading = true)
                    }

                    is Resource.Success -> {
                        _state.value = state.value.copy(
                            isLoading = false,
                            entry = result.data ?: state.value.entry
                        )
                    }
                }
            }.launchIn(this)
        }
    }

    private var onToggleBookmarkJob: Job? = null
    fun onToggleBookmark() {
        val id = state.value.entry.id ?: return
        onToggleBookmarkJob?.cancel()
        onToggleBookmarkJob = viewModelScope.launch {
            repository.toggleBookmarkEntry(id = id).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        _state.value = state.value.copy(isLoading = false)
                        result.uiText?.let { _eventFlow.emit(UIEvent.ShowSnackbar(it)) }
                    }

                    is Resource.Loading -> {
                        _state.value = state.value.copy(isLoading = true)
                    }

                    is Resource.Success -> {
                        _state.value = state.value.copy(
                            isLoading = false,
                            entry = result.data ?: state.value.entry
                        )
                    }
                }
            }.launchIn(this)
        }
    }
}