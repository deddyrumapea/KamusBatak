package com.romnan.kamusbatak.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.romnan.kamusbatak.R

@Composable
fun DefaultTopBar(
    title: @Composable () -> String,
    onOpenDrawer: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: @Composable () -> Color = { MaterialTheme.colors.primary },
) = TopAppBar(
    backgroundColor = backgroundColor(),
    modifier = modifier.fillMaxWidth(),
    elevation = 0.dp,
) {
    IconButton(onClick = onOpenDrawer) {
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = stringResource(R.string.menu),
            tint = contentColorFor(backgroundColor = backgroundColor()),
        )
    }

    Text(
        text = title(),
        style = MaterialTheme.typography.h6,
        color = contentColorFor(backgroundColor = backgroundColor()),
    )
}