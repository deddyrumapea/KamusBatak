package com.romnan.kamusbatak.feature_dictionary.domain.repository

import com.romnan.kamusbatak.core.util.Resource
import com.romnan.kamusbatak.feature_dictionary.data.util.Language
import com.romnan.kamusbatak.feature_dictionary.domain.model.Entry
import kotlinx.coroutines.flow.Flow

interface DictionaryRepository {
    fun searchEntries(keyword: String, kwLanguage: Language): Flow<Resource<List<Entry>>>
}