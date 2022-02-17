package com.romnan.kamusbatak.core.util

sealed class Language {
    abstract val fullName: String
    abstract val codename: String

    object Ind : Language() {
        override val fullName: String = "Indonesia"
        override val codename: String = "ind"
    }

    object Btk : Language() {
        override val fullName: String = "Batak"
        override val codename: String = "btk"
    }
}
