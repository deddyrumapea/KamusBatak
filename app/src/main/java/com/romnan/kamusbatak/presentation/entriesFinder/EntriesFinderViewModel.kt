package com.romnan.kamusbatak.presentation.entriesFinder

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romnan.kamusbatak.R
import com.romnan.kamusbatak.domain.model.Entry
import com.romnan.kamusbatak.domain.repository.DictionaryRepository
import com.romnan.kamusbatak.domain.util.Constants
import com.romnan.kamusbatak.domain.util.Resource
import com.romnan.kamusbatak.domain.util.UIText
import com.romnan.kamusbatak.presentation.util.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import logcat.logcat
import javax.inject.Inject

@HiltViewModel
class EntriesFinderViewModel @Inject constructor(
    private val dictionaryRepository: DictionaryRepository,
) : ViewModel() {

    private val _searchQuery = mutableStateOf(TextFieldValue())
    val searchQuery: State<TextFieldValue> = _searchQuery

    private val _entries = mutableStateOf(emptyList<Entry>())
    val entries: State<List<Entry>> = _entries

    private val _state = mutableStateOf(EntriesFinderScreenState.defaultValue)
    val state: State<EntriesFinderScreenState> = _state

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        updateLocalDb()
    }

    fun onSwapLanguages() {
        _state.value = state.value.copy(
            sourceLanguage = state.value.targetLanguage,
            targetLanguage = state.value.sourceLanguage
        )
        fetchEntries()
    }

    fun onQueryChange(newValue: TextFieldValue) {
        val oldValue = searchQuery.value
        _searchQuery.value = newValue
        if (oldValue.text != newValue.text) fetchEntries()
    }

    fun onRefocusSearchQuery() {
        _searchQuery.value = searchQuery.value.copy(
            selection = TextRange(index = searchQuery.value.text.length)
        )
    }

    fun onOpenOptionsMenu() {
        _state.value = state.value.copy(isOptionsMenuVisible = true)
    }

    fun onCloseOptionsMenu() {
        _state.value = state.value.copy(isOptionsMenuVisible = false)
    }

    private var updateLocalDbJob: Job? = null
    private fun updateLocalDb() {
        updateLocalDbJob?.cancel()
        updateLocalDbJob = viewModelScope.launch {
            dictionaryRepository
                .updateLocalDb()
                .onEach { result ->
                    logcat { "downloadUpdate ${result::class.java.simpleName}" }
                    if (result is Resource.Error) {
                        delay(Constants.DURATION_DOWNLOAD_UPDATE_COOLDOWN)
                        updateLocalDb()
                    }
                }
                .launchIn(this)
        }
    }

    private var fetchEntriesJob: Job? = null
    private fun fetchEntries() {
        fetchEntriesJob?.cancel()
        fetchEntriesJob = viewModelScope.launch {
            delay(Constants.DURATION_SEARCH_DELAY)
            dictionaryRepository.getEntries(
                keyword = searchQuery.value.text,
                srcLang = state.value.sourceLanguage,
            ).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _entries.value = result.data ?: emptyList()
                        _state.value = state.value.copy(isLoadingEntries = false)
                    }
                    is Resource.Loading -> {
                        _entries.value = result.data ?: emptyList()
                        _state.value = state.value.copy(isLoadingEntries = true)
                    }
                    is Resource.Error -> {
                        _entries.value = result.data ?: emptyList()
                        _state.value = state.value.copy(isLoadingEntries = false)
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