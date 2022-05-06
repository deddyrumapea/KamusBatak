package com.romnan.kamusbatak.featEntriesFinder.domain.repository

import com.romnan.kamusbatak.core.domain.model.Entry
import com.romnan.kamusbatak.core.domain.model.Language
import com.romnan.kamusbatak.core.util.Resource
import kotlinx.coroutines.flow.Flow

interface EntriesFinderRepository {
    fun getEntries(keyword: String, srcLang: Language): Flow<Resource<List<Entry>>>
}