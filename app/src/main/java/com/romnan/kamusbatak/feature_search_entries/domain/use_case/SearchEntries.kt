package com.romnan.kamusbatak.feature_search_entries.domain.use_case

import com.romnan.kamusbatak.core.util.Resource
import com.romnan.kamusbatak.core.util.Language
import com.romnan.kamusbatak.feature_search_entries.domain.model.Entry
import com.romnan.kamusbatak.feature_search_entries.domain.repository.DictionaryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchEntries(
    private val repository: DictionaryRepository
) {
    operator fun invoke(keyword: String, kwLanguage: Language): Flow<Resource<List<Entry>>> {
        if (keyword.isBlank()) return flow { }

        return repository.searchEntries(keyword, kwLanguage)
    }
}