package com.romnan.kamusbatak.data.repository

import com.romnan.kamusbatak.data.datastore.AppPreferencesManager
import com.romnan.kamusbatak.domain.model.ThemeMode
import com.romnan.kamusbatak.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesRepositoryImpl(
    private val appPreferencesManager: AppPreferencesManager
) : PreferencesRepository {

    override val themeMode: Flow<ThemeMode>
        get() = appPreferencesManager.dataStore.data.map {
            try {
                ThemeMode.valueOf(it.themeModeName)
            } catch (e: IllegalArgumentException) {
                ThemeMode.System
            }
        }

    override suspend fun setThemeMode(themeMode: ThemeMode) {
        appPreferencesManager.dataStore.updateData {
            it.copy(themeModeName = themeMode.name)
        }
    }
}