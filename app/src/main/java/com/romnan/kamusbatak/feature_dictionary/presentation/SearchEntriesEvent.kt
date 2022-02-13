package com.romnan.kamusbatak.feature_dictionary.presentation

sealed class SearchEntriesEvent {
    data class QueryChange(val query: String) : SearchEntriesEvent()
    object LanguagesSwap : SearchEntriesEvent()
}