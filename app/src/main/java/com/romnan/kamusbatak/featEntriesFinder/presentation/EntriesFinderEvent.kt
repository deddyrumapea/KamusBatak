package com.romnan.kamusbatak.featEntriesFinder.presentation

sealed class EntriesFinderEvent {
    data class QueryChange(val query: String) : EntriesFinderEvent()
    data class SetShowOptionsMenu(val show: Boolean) : EntriesFinderEvent()
    object LanguagesSwap : EntriesFinderEvent()
}