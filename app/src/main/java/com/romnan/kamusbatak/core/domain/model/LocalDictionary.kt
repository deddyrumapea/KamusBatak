package com.romnan.kamusbatak.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LocalDictionary(
    val lastUpdated: Long?
) {
    val isAvailable: Boolean get() = lastUpdated != null
}