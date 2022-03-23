package com.romnan.kamusbatak.core.presentation.util

sealed class UIEvent {
    data class ShowSnackbar(val message: String) : UIEvent()
}