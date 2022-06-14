package com.romnan.kamusbatak.domain.repository

import com.romnan.kamusbatak.domain.model.Entry
import com.romnan.kamusbatak.domain.model.Language
import com.romnan.kamusbatak.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface EntriesFinderRepository {
    fun getEntries(keyword: String, srcLang: Language): Flow<Resource<List<Entry>>>
}