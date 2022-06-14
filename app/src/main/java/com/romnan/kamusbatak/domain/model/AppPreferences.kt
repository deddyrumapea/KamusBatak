package com.romnan.kamusbatak.domain.model

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