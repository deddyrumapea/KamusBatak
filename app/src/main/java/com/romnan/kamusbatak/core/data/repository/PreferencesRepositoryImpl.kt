package com.romnan.kamusbatak.core.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.romnan.kamusbatak.core.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesRepositoryImpl(
    appContext: Context
) : PreferencesRepository {

    private val Context.dataStore by preferencesDataStore(PREF_NAME)
    private val dataStore = appContext.dataStore
    private val keyLastOfflineSupportUpdatedAt =
        longPreferencesKey(KEY_NAME_LAST_OFFLINE_SUPPORT_UPDATED_AT)

    override fun getLastOfflineSupportUpdatedAt(): Flow<Long?> {
        return dataStore.data.map { pref ->
            pref[keyLastOfflineSupportUpdatedAt]
        }
    }

    override suspend fun setLastOfflineSupportUpdatedAt(timeInMillis: Long) {
        dataStore.edit { pref ->
            pref[keyLastOfflineSupportUpdatedAt] = timeInMillis
        }
    }

    companion object {
        private const val KEY_NAME_LAST_OFFLINE_SUPPORT_UPDATED_AT =
            "KEY_NAME_LAST_OFFLINE_SUPPORT_UPDATED_AT"
        const val PREF_NAME = "PREF_KAMUS_BATAK"
    }
}