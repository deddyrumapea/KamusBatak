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

    @Query("DELETE FROM entryentity WHERE btkWord IN (:btkWords)")
    suspend fun deleteEntries(btkWords: List<String>)

    @Query("SELECT * FROM entryentity WHERE btkWord LIKE '%' || :keyword || '%'")
    suspend fun searchWithBatakKeyword(keyword: String): List<EntryEntity>

    // TODO: add search with Bahasa Indonesia keyword
}