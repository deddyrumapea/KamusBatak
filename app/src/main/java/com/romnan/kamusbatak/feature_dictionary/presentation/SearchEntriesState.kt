package com.romnan.kamusbatak.feature_dictionary.presentation

import com.romnan.kamusbatak.feature_dictionary.data.util.Language
import com.romnan.kamusbatak.feature_dictionary.domain.model.Entry

data class SearchEntriesState(
    val entries: List<Entry> = emptyList(),
    val isLoading: Boolean = false,
    val sourceLanguage: Language = Language.Btk,
    val targetLanguage: Language = Language.Ind
)