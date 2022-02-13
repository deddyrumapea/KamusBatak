package com.romnan.kamusbatak.feature_dictionary.domain.use_case

import com.romnan.kamusbatak.core.util.Resource
import com.romnan.kamusbatak.feature_dictionary.data.util.Language
import com.romnan.kamusbatak.feature_dictionary.domain.model.Entry
import com.romnan.kamusbatak.feature_dictionary.domain.repository.DictionaryRepository
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