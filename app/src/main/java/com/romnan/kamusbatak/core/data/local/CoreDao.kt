package com.romnan.kamusbatak.core.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.romnan.kamusbatak.core.data.local.entity.EntryEntity

@Dao
interface CoreDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntries(cachedEntries: List<EntryEntity>)

    @Query("SELECT * FROM entryentity WHERE srcLang = :srcLangCodeName AND word LIKE :keyword ORDER BY word ASC")
    suspend fun getEntries(
        keyword: String,
        srcLangCodeName: String
    ): List<EntryEntity>

    @Query("SELECT updatedAt FROM entryentity ORDER BY updatedAt DESC LIMIT 1")
    suspend fun getLatestEntryUpdatedAt(): String?
}