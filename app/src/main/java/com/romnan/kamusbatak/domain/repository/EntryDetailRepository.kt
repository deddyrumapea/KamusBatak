package com.romnan.kamusbatak.domain.repository

import com.romnan.kamusbatak.domain.model.Entry
import com.romnan.kamusbatak.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface EntryDetailRepository {
    fun getEntry(id: Int): Flow<Resource<Entry>>
    fun toggleBookmarkEntry(id: Int): Flow<Resource<Entry>>
}