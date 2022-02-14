package com.romnan.kamusbatak.core.util

sealed class Language {
    abstract val fullName: String

    object Ind : Language() {
        override val fullName: String = "Indonesia"
    }

    object Btk : Language() {
        override val fullName: String = "Batak"
    }
}
