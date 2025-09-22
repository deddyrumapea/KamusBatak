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

    @Query(
        """
        SELECT * 
        FROM dict_entries 
        WHERE sourceLang = :srcLangCodeName 
        AND deletedAt IS NULL
        AND (
            headword LIKE :keyword || '%' 
            OR headword LIKE '%' || :keyword || '%' 
            OR definitions LIKE '%' || :keyword || '%'
            OR definitions LIKE :keyword || '%'
        ) 
        ORDER BY 
            CASE 
                WHEN headword LIKE :keyword || '%' THEN 1 
                WHEN headword LIKE '%' || :keyword || '%' THEN 2 
                ELSE 3 
            END, 
            headword;
    """
    )
    suspend fun findByKeyword(
        keyword: String,
        srcLangCodeName: String
    ): List<EntryEntity>

    @Query(
        """
        SELECT updatedAt 
        FROM dict_entries 
        ORDER BY updatedAt DESC 
        LIMIT 1
    """
    )
    suspend fun getLatestEntryUpdatedAt(): String?

    @Query(
        """
        UPDATE dict_entries 
        SET bookmarkedAt = 
            CASE 
                WHEN bookmarkedAt IS NULL THEN CURRENT_TIMESTAMP 
                ELSE NULL 
            END 
        WHERE id = :id
    """
    )
    suspend fun toggleBookmark(id: Int)

    @Query(
        """
        SELECT * 
        FROM dict_entries 
        WHERE id = :id
            AND deletedAt IS NULL
    """
    )
    suspend fun findById(id: Int): EntryEntity?

    @Query(
        """
        SELECT * 
        FROM dict_entries 
        WHERE bookmarkedAt IS NOT NULL 
            AND sourceLang = :srcLangCodeName 
            AND deletedAt IS NULL 
        ORDER BY bookmarkedAt DESC
    """
    )
    suspend fun findBookmarked(
        srcLangCodeName: String,
    ): List<EntryEntity>

    @Query(
        """
        SELECT * 
        FROM dict_entries 
        WHERE sourceLang =:srcLangCodeName
            AND deletedAt IS NULL
        ORDER BY RANDOM()
        LIMIT :count
    """
    )
    suspend fun getRandomEntries(
        count: Int,
        srcLangCodeName: String,
    ): List<EntryEntity>
}