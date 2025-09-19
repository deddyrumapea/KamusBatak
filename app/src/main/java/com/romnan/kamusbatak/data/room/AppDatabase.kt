package com.romnan.kamusbatak.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.romnan.kamusbatak.data.room.entity.EntryEntity

@Database(
    entities = [EntryEntity::class],
    version = 4,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract val entryDao: EntryDao

    companion object {
        const val NAME = "db_kamus_batak"
    }
}