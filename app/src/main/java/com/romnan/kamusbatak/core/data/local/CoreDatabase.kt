package com.romnan.kamusbatak.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.romnan.kamusbatak.core.data.local.entity.EntryEntity

@Database(
    entities = [EntryEntity::class],
    version = 2,
    exportSchema = false,
)
abstract class CoreDatabase : RoomDatabase() {
    abstract val dao: CoreDao

    companion object {
        const val NAME = "db_kamus_batak"
    }
}