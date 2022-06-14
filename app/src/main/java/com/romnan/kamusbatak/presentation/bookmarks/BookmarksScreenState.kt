package com.romnan.kamusbatak.presentation.bookmarks

import com.romnan.kamusbatak.domain.model.Entry

data class BookmarksScreenState(
    val isLoadingBtkEntries: Boolean,
    val isLoadingIndEntries: Boolean,
    val indEntries: List<Entry>,
    val btkEntries: List<Entry>,
) {
    companion object {
        val defaultValue = BookmarksScreenState(
            isLoadingBtkEntries = false,
            isLoadingIndEntries = false,
            indEntries = emptyList(),
            btkEntries = emptyList(),
        )
    }
}