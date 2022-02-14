package com.romnan.kamusbatak.feature_search_entries.presentation

import com.romnan.kamusbatak.feature_search_entries.data.util.Language
import com.romnan.kamusbatak.feature_search_entries.domain.model.Entry

data class SearchEntriesState(
    val entries: List<Entry> = emptyList(),
    val isLoading: Boolean = false,
    val sourceLanguage: Language = Language.Btk,
    val targetLanguage: Language = Language.Ind
)