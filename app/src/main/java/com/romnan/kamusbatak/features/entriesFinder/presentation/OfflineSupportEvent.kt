package com.romnan.kamusbatak.features.entriesFinder.presentation

sealed class OfflineSupportEvent {
    object DownloadUpdate : OfflineSupportEvent()
}
