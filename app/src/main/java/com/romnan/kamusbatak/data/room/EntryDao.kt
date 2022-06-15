package com.romnan.kamusbatak.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.romnan.kamusbatak.data.room.entity.EntryEntity

@Dao
interface EntryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cachedEntries: List<EntryEntity>)

    @Query("SELECT * FROM entryentity WHERE srcLang = :srcLangCodeName AND (word LIKE :keyword || '%' OR word LIKE '%' || :keyword || '%') ORDER BY CASE WHEN word LIKE :keyword || '%' THEN 1 ELSE 2 END, word")
    suspend fun findByKeyword(
        keyword: String,
        srcLangCodeName: String
    ): List<EntryEntity>

    @Query("SELECT updatedAt FROM entryentity ORDER BY updatedAt DESC LIMIT 1")
    suspend fun getLatestEntryUpdatedAt(): String?

    @Query("UPDATE entryentity SET bookmarkedAt = CASE WHEN bookmarkedAt IS NULL THEN CURRENT_TIMESTAMP ELSE NULL END WHERE id = :id")
    suspend fun toggleBookmark(id: Int)

    @Query("SELECT * FROM entryentity WHERE id = :id")
    suspend fun findById(id: Int): EntryEntity?

    @Query("SELECT * FROM entryentity WHERE bookmarkedAt IS NOT NULL AND srcLang = :srcLangCodeName ORDER BY bookmarkedAt DESC")
    suspend fun findBookmarked(
        srcLangCodeName: String,
    ): List<EntryEntity>
}