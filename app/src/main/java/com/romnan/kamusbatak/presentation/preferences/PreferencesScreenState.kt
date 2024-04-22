package com.romnan.kamusbatak.presentation.preferences

import com.romnan.kamusbatak.BuildConfig
import com.romnan.kamusbatak.domain.model.ThemeMode

data class PreferencesScreenState(
    val isUpdatingLocalDb: Boolean = false,
    val localDbLastUpdatedAt: Long? = null,

    val currentThemeMode: ThemeMode = ThemeMode.System,
    val isThemeModeDialogVisible: Boolean = false,

    val visiblePermissionDialogQueue: List<String> = emptyList(),

    val isAppRatingDialogVisible: Boolean = false,
) {
    val appVersion: String
        get() = BuildConfig.VERSION_NAME
}