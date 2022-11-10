package com.romnan.kamusbatak.domain.model

data class QuizItem(
    val question: String,
    val options: List<String>,
    val answerKeyIdx: Int,
)