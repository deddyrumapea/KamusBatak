package com.romnan.kamusbatak.presentation.quizGame

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romnan.kamusbatak.R
import com.romnan.kamusbatak.domain.repository.DictionaryRepository
import com.romnan.kamusbatak.domain.util.Constants
import com.romnan.kamusbatak.domain.util.Resource
import com.romnan.kamusbatak.domain.util.UIText
import com.romnan.kamusbatak.presentation.quizGame.model.QuizItemPresentation
import com.romnan.kamusbatak.presentation.quizGame.model.toPresentation
import com.romnan.kamusbatak.presentation.util.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import logcat.logcat
import javax.inject.Inject

@HiltViewModel
class QuizPlayingViewModel @Inject constructor(
    private val dictionaryRepository: DictionaryRepository,
) : ViewModel() {

    var correctAnswersCount by mutableStateOf(0)
        private set

    var quizGameName by mutableStateOf<String?>(null)
        private set

    var isShowingAnswer by mutableStateOf(false)
        private set

    var isLoading by mutableStateOf(false)
        private set

    val isAnswerCorrect
        get() = selectedOptionIdx == currQuizItem.correctOptionIdx

    var currQuizItem by mutableStateOf(QuizItemPresentation.defaultValue)
        private set

    var selectedOptionIdx by mutableStateOf(-1)
        private set

    val quizItemsSize = Constants.QUIZ_ITEMS_SIZE

    private val _eventFlow = Channel<UIEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    fun onReceivedQuizGameName(value: String) {
        quizGameName = value
        getNextQuizItem()
    }

    fun onClickOption(index: Int) {
        if (isShowingAnswer) return
        selectedOptionIdx = index
    }

    fun onClickNext() {
        if (isAnswerCorrect) {
            ++correctAnswersCount
            logcat { "Correct answer, correctAnswersCount: $correctAnswersCount" }
        }

        if (currQuizItem.number >= quizItemsSize) return

        getNextQuizItem()
        selectedOptionIdx = -1
        isShowingAnswer = false
    }

    suspend fun onClickCheck() {
        if (selectedOptionIdx == -1) {
            _eventFlow.send(UIEvent.ShowSnackbar(UIText.StringResource(R.string.em_option_not_selected)))
            return
        }
        isShowingAnswer = true
    }

    private var getNextQuizItemJob: Job? = null
    private fun getNextQuizItem() {
        getNextQuizItemJob?.cancel()
        getNextQuizItemJob = viewModelScope.launch {
            val qgName = quizGameName ?: return@launch
            dictionaryRepository.getQuizItem(quizGameName = qgName).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        isLoading = false
                        result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
                    }

                    is Resource.Loading -> isLoading = true

                    is Resource.Success -> {
                        isLoading = false
                        result.data?.let {
                            currQuizItem = it.toPresentation(number = currQuizItem.number + 1)
                            logcat { "answerKeyIdx: ${it.answerKeyIdx}" }
                        }
                    }
                }
            }.launchIn(this)
        }
    }
}