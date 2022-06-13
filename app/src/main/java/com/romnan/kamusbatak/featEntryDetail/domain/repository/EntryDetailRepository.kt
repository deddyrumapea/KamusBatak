package com.romnan.kamusbatak.featEntryDetail.domain.repository

import com.romnan.kamusbatak.core.domain.model.Entry
import com.romnan.kamusbatak.core.util.Resource
import kotlinx.coroutines.flow.Flow

interface EntryDetailRepository {
    fun getEntry(id: Int): Flow<Resource<Entry>>
    fun toggleBookmarkEntry(id: Int): Flow<Resource<Entry>>
}