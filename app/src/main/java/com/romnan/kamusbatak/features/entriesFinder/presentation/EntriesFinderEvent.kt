package com.romnan.kamusbatak.features.entriesFinder.presentation

sealed class EntriesFinderEvent {
    data class QueryChange(val query: String) : EntriesFinderEvent()
    data class SetShowOptionsMenu(val show: Boolean) : EntriesFinderEvent()
    data class SetShowUpdateDialog(val show: Boolean) : EntriesFinderEvent()
    object LanguagesSwap : EntriesFinderEvent()
}