package com.romnan.kamusbatak.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AppPreferences(
    val localDictionary: LocalDictionary,
) {
    companion object {
        val defaultValue = AppPreferences(
            localDictionary = LocalDictionary(
                lastUpdated = null
            )
        )
    }
}