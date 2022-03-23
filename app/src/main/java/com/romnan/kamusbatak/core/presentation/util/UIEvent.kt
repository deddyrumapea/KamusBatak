package com.romnan.kamusbatak.core.presentation.util

import com.romnan.kamusbatak.core.util.UIText

sealed class UIEvent {
    data class ShowSnackbar(val uiText: UIText) : UIEvent()
}