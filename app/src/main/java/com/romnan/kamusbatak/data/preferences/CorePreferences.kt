package com.romnan.kamusbatak.data.preferences

import android.content.Context
import androidx.datastore.dataStore

class CorePreferences(
    appContext: Context
) {
    val dataStore = appContext.dataStore

    companion object {
        private const val APP_PREF_FILE_NAME = "kamus_batak_pref.json"
        private val Context.dataStore by dataStore(
            fileName = APP_PREF_FILE_NAME,
            serializer = AppPreferencesSerializer,
        )
    }
}