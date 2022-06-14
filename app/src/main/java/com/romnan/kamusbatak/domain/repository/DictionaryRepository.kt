package com.romnan.kamusbatak.domain.repository

import com.romnan.kamusbatak.domain.model.Entry
import com.romnan.kamusbatak.domain.model.Language
import com.romnan.kamusbatak.domain.util.Resource
import com.romnan.kamusbatak.domain.util.SimpleResource
import kotlinx.coroutines.flow.Flow

interface DictionaryRepository {
    val localDbLastUpdatedAt: Flow<Long?>

    fun getEntries(
        keyword: String,
        srcLang: Language,
    ): Flow<Resource<List<Entry>>>

    fun getEntry(
        id: Int,
    ): Flow<Resource<Entry>>

    fun getBookmarkedEntries(
        srcLang: Language,
    ): Flow<Resource<List<Entry>>>

    fun toggleBookmarkEntry(
        id: Int,
    ): Flow<Resource<Entry>>

    fun updateLocalDb(): Flow<SimpleResource>
}