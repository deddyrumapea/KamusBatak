package com.romnan.kamusbatak.feature_entries_finder.presentation

data class OfflineSupportState(
    val isUpdating: Boolean = false,
    val isUpToDate: Boolean = false,
    val isErrorUpdating: Boolean = false,
    val lastUpdated: Long? = null
)