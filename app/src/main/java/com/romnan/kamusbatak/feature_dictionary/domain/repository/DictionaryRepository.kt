package com.romnan.kamusbatak.feature_dictionary.domain.repository

import com.romnan.kamusbatak.core.util.Resource
import com.romnan.kamusbatak.feature_dictionary.domain.model.Entry
import kotlinx.coroutines.flow.Flow

interface DictionaryRepository {
    fun searchWithBatakKeyword(keyword: String): Flow<Resource<List<Entry>>>
}