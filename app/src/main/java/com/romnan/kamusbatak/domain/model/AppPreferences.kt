package com.romnan.kamusbatak.domain.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class AppPreferences(
    val localDbLastUpdatedAt: Long = 1758326400000L,
    val themeModeName: String = ThemeMode.System.name,
    val dailyWordTimeInMillis: Long? = null,
)