package com.romnan.kamusbatak.presentation.entriesFinder

import com.romnan.kamusbatak.domain.model.Language

data class EntriesFinderScreenState(
    val isLoadingEntries: Boolean,
    val sourceLanguage: Language,
    val targetLanguage: Language,
    val isOptionsMenuVisible: Boolean,
) {
    companion object {
        val defaultValue = EntriesFinderScreenState(
            isLoadingEntries = false,
            sourceLanguage = Language.Btk,
            targetLanguage = Language.Ind,
            isOptionsMenuVisible = false,
        )
    }
}