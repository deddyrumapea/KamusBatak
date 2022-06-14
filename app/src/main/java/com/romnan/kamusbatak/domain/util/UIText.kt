package com.romnan.kamusbatak.domain.util

import androidx.annotation.StringRes

sealed class UIText {
    data class DynamicString(val value: String) : UIText()
    data class StringResource(@StringRes val id: Int) : UIText()
}