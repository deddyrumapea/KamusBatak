package com.romnan.kamusbatak.feature_search_entries.domain.use_case

import com.romnan.kamusbatak.core.util.Language
import com.romnan.kamusbatak.core.util.Resource
import com.romnan.kamusbatak.feature_search_entries.domain.model.Entry
import com.romnan.kamusbatak.feature_search_entries.domain.repository.DictionaryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchEntries(
    private val repository: DictionaryRepository
) {
    operator fun invoke(keyword: String, srcLang: Language): Flow<Resource<List<Entry>>> {
        if (keyword.isBlank()) return flow { }
        val kw = if (keyword.length > 3) "%${keyword.lowercase()}%" else "${keyword.lowercase()}%"
        return repository.searchEntries(keyword = kw, srcLang = srcLang)
    }
}