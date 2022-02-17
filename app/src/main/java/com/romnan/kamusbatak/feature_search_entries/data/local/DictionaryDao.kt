package com.romnan.kamusbatak.feature_search_entries.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.romnan.kamusbatak.feature_search_entries.data.local.entity.EntryEntity

@Dao
interface DictionaryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntries(entries: List<EntryEntity>)

    @Query("SELECT * FROM entryentity WHERE srcLang = :srcLang AND word LIKE :keyword")
    suspend fun getEntries(keyword: String, srcLang: String): List<EntryEntity>
}