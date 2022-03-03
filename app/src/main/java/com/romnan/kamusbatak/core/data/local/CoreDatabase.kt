package com.romnan.kamusbatak.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.romnan.kamusbatak.core.data.local.entity.CachedEntryEntity

@Database(
    entities = [CachedEntryEntity::class],
    version = 1
)
abstract class CoreDatabase : RoomDatabase() {
    abstract val dao: CoreDao

    companion object {
        const val NAME = "db_kamus_batak"
    }
}