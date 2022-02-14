package com.romnan.kamusbatak.feature_search_entries.data.util

sealed class Language {
    abstract val fullName: String

    object Ind : Language() {
        override val fullName: String = "Indonesia"
    }

    object Btk : Language() {
        override val fullName: String = "Batak"
    }
}
