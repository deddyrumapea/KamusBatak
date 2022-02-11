package com.romnan.kamusbatak.feature_dictionary.domain.model

data class Entry(
    val btkWord: String,
    val indWord: String,
    val dialect: String?,
    val phonetic: String?,
    val example: String?
)
