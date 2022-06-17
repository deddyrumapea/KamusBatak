package com.romnan.kamusbatak.domain.model

data class Entry(
    val id: Int?,
    val word: String,
    val meaning: String,
    val isBookmarked: Boolean,
)
