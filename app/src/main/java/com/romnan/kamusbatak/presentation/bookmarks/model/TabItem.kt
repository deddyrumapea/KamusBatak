package com.romnan.kamusbatak.presentation.bookmarks.model

import androidx.compose.runtime.Composable

data class TabItem(
    val title: String,
    val content: @Composable () -> Unit
)