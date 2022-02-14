package com.romnan.kamusbatak.feature_search_entries.domain.model

data class Entry(
    val btkWord: String,
    val indWord: String,
    val dialect: String?,
    val phonetic: String?,
    val example: String?
)
