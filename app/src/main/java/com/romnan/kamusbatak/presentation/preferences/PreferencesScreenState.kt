package com.romnan.kamusbatak.presentation.preferences

import com.romnan.kamusbatak.BuildConfig
import com.romnan.kamusbatak.domain.model.ThemeMode

data class PreferencesScreenState(
    val isUpdatingLocalDb: Boolean,
    val localDbLastUpdatedAt: Long?,
    val currentThemeMode: ThemeMode,
    val isThemeModeDialogVisible: Boolean,
) {
    val appVersion: String
        get() = BuildConfig.VERSION_NAME

    companion object {
        val defaultValue = PreferencesScreenState(
            isUpdatingLocalDb = false,
            localDbLastUpdatedAt = null,
            currentThemeMode = ThemeMode.System,
            isThemeModeDialogVisible = false,
        )
    }
}