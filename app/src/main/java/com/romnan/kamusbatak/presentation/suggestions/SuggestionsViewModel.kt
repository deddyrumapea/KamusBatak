package com.romnan.kamusbatak.presentation.suggestions

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romnan.kamusbatak.R
import com.romnan.kamusbatak.domain.model.Language
import com.romnan.kamusbatak.domain.model.Suggestion
import com.romnan.kamusbatak.domain.repository.DictionaryRepository
import com.romnan.kamusbatak.domain.util.Resource
import com.romnan.kamusbatak.domain.util.UIText
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
class SuggestionsViewModel @Inject constructor(
    private val dictionaryRepository: DictionaryRepository,
) : ViewModel() {

    var isLoadingEntry by mutableStateOf(false)
        private set

    var isLoadingSubmit by mutableStateOf(false)
        private set

    private var oldWord by mutableStateOf("")
    var word by mutableStateOf("")
        private set

    private var oldMeaning by mutableStateOf("")
    var meaning by mutableStateOf("")
        private set

    private val _eventFlow = Channel<UIEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    var entryId by mutableStateOf<Int?>(null)
        private set

    var srcLang by mutableStateOf<Language>(Language.BTK)
        private set

    fun onChangeWord(value: String) {
        word = value
    }

    fun onChangeMeaning(value: String) {
        meaning = value
    }

    fun onChangeSrcLang(value: Language) {
        srcLang = value
    }

    private var onClickSubmitJob: Job? = null
    fun onClickSubmit() {
        onClickSubmitJob?.cancel()
        onClickSubmitJob = viewModelScope.launch {
            val suggestion = entryId?.let {
                Suggestion.OldEntry(
                    entryId = it,
                    oldWord = oldWord,
                    word = word,
                    oldMeaning = oldMeaning,
                    meaning = meaning,
                )
            } ?: Suggestion.NewEntry(
                srcLang = srcLang,
                word = word,
                meaning = meaning
            )

            dictionaryRepository.postSuggestion(
                suggestion = suggestion,
            ).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        isLoadingSubmit = false
                        result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
                    }

                    is Resource.Loading -> isLoadingSubmit = true

                    is Resource.Success -> {
                        isLoadingSubmit = false
                        word = ""
                        meaning = ""
                        entryId = null
                        _eventFlow.send(
                            UIEvent.ShowSnackbar(UIText.StringResource(R.string.sm_suggestions))
                        )
                    }
                }
            }.launchIn(this)
        }
    }

    private var onReceivedEntryIdJob: Job? = null
    fun onReceivedEntryId(value: Int) {
        onReceivedEntryIdJob?.cancel()
        onReceivedEntryIdJob = viewModelScope.launch {
            dictionaryRepository.getEntry(id = value).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        isLoadingEntry = false
                        result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
                    }

                    is Resource.Loading -> isLoadingEntry = true

                    is Resource.Success -> {
                        isLoadingEntry = false
                        result.data?.let {
                            entryId = it.id
                            oldWord = it.word
                            word = it.word
                            oldMeaning = it.meaning
                            meaning = it.meaning
                        }
                    }
                }
            }.launchIn(this)
        }
    }
}