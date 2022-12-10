package com.romnan.kamusbatak.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AppPreferences(
    val localDbLastUpdatedAt: Long?,
    val themeModeName: String,
    val dailyWordTimeInMillis: Long?,
) {
    companion object {
        val defaultValue = AppPreferences(
            localDbLastUpdatedAt = null,
            themeModeName = ThemeMode.System.name,
            dailyWordTimeInMillis = null,
        )
    }
}