package com.romnan.kamusbatak.feature_entries_finder.presentation

sealed class OfflineSupportEvent {
    object DownloadUpdate : OfflineSupportEvent()
}
