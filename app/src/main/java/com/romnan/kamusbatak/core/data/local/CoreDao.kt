package com.romnan.kamusbatak.core.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.romnan.kamusbatak.core.data.local.entity.CachedEntryEntity

@Dao
interface CoreDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCachedEntries(cachedEntries: List<CachedEntryEntity>)

    @Query("SELECT * FROM cachedentryentity WHERE srcLang = :srcLang AND word LIKE :keyword ORDER BY word ASC")
    suspend fun getCachedEntries(keyword: String, srcLang: String): List<CachedEntryEntity>

    @Query("SELECT updatedAt FROM cachedentryentity ORDER BY updatedAt DESC LIMIT 1")
    suspend fun getLatestEntryUpdatedAt(): String?
}