package com.romnan.kamusbatak.featEntriesFinder.presentation

import com.romnan.kamusbatak.core.domain.model.Entry
import com.romnan.kamusbatak.core.domain.model.Language

data class EntriesFinderState(
    val isLoadingEntries: Boolean = false,
    val sourceLanguage: Language = Language.Btk,
    val targetLanguage: Language = Language.Ind,
    val isOptionsMenuShown: Boolean = false
)