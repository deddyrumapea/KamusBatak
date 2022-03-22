package com.romnan.kamusbatak.feature_entries_finder.domain.repository

import com.romnan.kamusbatak.core.domain.model.Entry
import com.romnan.kamusbatak.core.util.Language
import com.romnan.kamusbatak.core.util.Resource
import com.romnan.kamusbatak.core.util.SimpleResource
import kotlinx.coroutines.flow.Flow

interface EntriesFinderRepository {
    fun getEntries(keyword: String, srcLang: Language): Flow<Resource<List<Entry>>>
}