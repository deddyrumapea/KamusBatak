package com.romnan.kamusbatak.feature_search_entries.presentation

sealed class SearchEntriesEvent {
    data class QueryChange(val query: String) : SearchEntriesEvent()
    object LanguagesSwap : SearchEntriesEvent()
}