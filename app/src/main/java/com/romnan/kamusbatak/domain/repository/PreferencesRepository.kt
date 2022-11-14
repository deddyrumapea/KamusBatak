package com.romnan.kamusbatak.domain.repository

import com.romnan.kamusbatak.domain.model.ThemeMode
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    val themeMode: Flow<ThemeMode>

    suspend fun setThemeMode(themeMode: ThemeMode)

    suspend fun setDailyNotification(timeInMillis: Long?)
}