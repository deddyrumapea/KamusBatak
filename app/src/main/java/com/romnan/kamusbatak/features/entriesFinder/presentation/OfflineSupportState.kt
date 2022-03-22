package com.romnan.kamusbatak.features.entriesFinder.presentation

data class OfflineSupportState(
    val isUpdating: Boolean = false,
    val isUpToDate: Boolean = false,
    val isErrorUpdating: Boolean = false,
    val lastUpdated: Long? = null
)