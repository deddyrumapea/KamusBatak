package com.romnan.kamusbatak.feature_entries_finder.presentation

sealed class EntriesFinderEvent {
    data class QueryChange(val query: String) : EntriesFinderEvent()
    object LanguagesSwap : EntriesFinderEvent()
}