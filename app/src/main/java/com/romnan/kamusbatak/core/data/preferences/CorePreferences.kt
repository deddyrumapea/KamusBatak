package com.romnan.kamusbatak.core.data.preferences

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

class CorePreferences(
    appContext: Context
) {
    private val Context.dataStore by preferencesDataStore(CORE_PREF_NAME)
    val dataStore = appContext.dataStore

    companion object {
        private const val CORE_PREF_NAME = "pref_kamus_batak"
    }
}