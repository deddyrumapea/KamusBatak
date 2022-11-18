package com.romnan.kamusbatak.presentation.quizGame.model

import com.romnan.kamusbatak.domain.model.QuizItem

data class QuizItemPresentation(
    val number: Int,
    val entryId: Int?,
    val question: String,
    val options: List<String>,
    val correctOptionIdx: Int,
) {
    val correctOption
        get() = try {
            options[correctOptionIdx]
        } catch (e: Exception) {
            null
        }

    companion object {
        val defaultValue = QuizItemPresentation(
            number = 0,
            entryId = null,
            question = "",
            options = emptyList(),
            correctOptionIdx = 0,
        )
    }
}

fun QuizItem.toPresentation(
    number: Int,
) = QuizItemPresentation(
    number = number,
    entryId = this.entryId,
    question = this.question,
    options = this.options,
    correctOptionIdx = this.answerKeyIdx,
)