package com.romnan.kamusbatak.featEntriesFinder.presentation

import com.romnan.kamusbatak.core.domain.model.Language

data class EntriesFinderState(
    val isLoadingEntries: Boolean,
    val sourceLanguage: Language,
    val targetLanguage: Language,
    val isOptionsMenuVisible: Boolean,
) {
    companion object {
        val defaultValue = EntriesFinderState(
            isLoadingEntries = false,
            sourceLanguage = Language.Btk,
            targetLanguage = Language.Ind,
            isOptionsMenuVisible = false,
        )
    }
}