package com.romnan.kamusbatak.feature_dictionary.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.romnan.kamusbatak.feature_dictionary.data.local.entity.EntryEntity

@Dao
interface DictionaryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntries(entries: List<EntryEntity>)

    @Query("SELECT * FROM entryentity WHERE indWord LIKE :keyword || '%'")
    suspend fun getEntriesWithIndKeyword(keyword: String): List<EntryEntity>

    @Query("SELECT * FROM entryentity WHERE btkWord LIKE :keyword || '%'")
    suspend fun getEntriesWithBtkKeyword(keyword: String): List<EntryEntity>
}