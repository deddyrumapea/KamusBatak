package com.romnan.kamusbatak.presentation.util

import com.romnan.kamusbatak.domain.util.UIText

sealed class UIEvent {
    data class ShowSnackbar(val uiText: UIText) : UIEvent()
}