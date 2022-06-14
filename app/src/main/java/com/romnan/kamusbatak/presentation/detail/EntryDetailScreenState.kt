package com.romnan.kamusbatak.presentation.detail

import com.romnan.kamusbatak.domain.model.Entry

data class EntryDetailScreenState(
    val entry: Entry,
    val isLoading: Boolean,
) {
    companion object {
        val defaultValue = EntryDetailScreenState(
            entry = Entry(
                id = null,
                word = "",
                meaning = "",
                isBookmarked = false,
            ),
            isLoading = false,
        )
    }
}
