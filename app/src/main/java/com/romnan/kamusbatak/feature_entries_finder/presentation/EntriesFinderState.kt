package com.romnan.kamusbatak.feature_entries_finder.presentation

import com.romnan.kamusbatak.core.util.Language
import com.romnan.kamusbatak.core.domain.model.Entry

data class EntriesFinderState(
    val entries: List<Entry> = emptyList(),
    val isLoading: Boolean = false,
    val sourceLanguage: Language = Language.Btk,
    val targetLanguage: Language = Language.Ind
)