package com.romnan.kamusbatak.features.preferences.presentation

data class OfflineSupportState(
    val isUpdating: Boolean = false,
    val isUpToDate: Boolean = false,
    val isErrorUpdating: Boolean = false,
    val lastUpdated: String? = null
)