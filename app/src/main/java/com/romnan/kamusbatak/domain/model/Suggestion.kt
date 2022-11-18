package com.romnan.kamusbatak.domain.model

sealed class Suggestion {
    data class NewEntry(
        val srcLang: Language,
        val word: String,
        val meaning: String,
    ) : Suggestion()

    data class OldEntry(
        val entryId: Int,
        val oldWord: String,
        val word: String,
        val oldMeaning: String,
        val meaning: String,
    ) : Suggestion()
}