package com.romnan.kamusbatak.feature_search_entries.domain.repository

import com.romnan.kamusbatak.core.util.Resource
import com.romnan.kamusbatak.core.util.Language
import com.romnan.kamusbatak.feature_search_entries.domain.model.Entry
import kotlinx.coroutines.flow.Flow

interface DictionaryRepository {
    fun searchEntries(keyword: String, srcLang: Language): Flow<Resource<List<Entry>>>
}