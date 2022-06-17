package com.romnan.kamusbatak.presentation.bookmarks.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.romnan.kamusbatak.R


@Composable
fun BookmarksTopBar(
    modifier: Modifier = Modifier,
    onOpenDrawer: () -> Unit,
) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.surface,
        modifier = modifier.fillMaxWidth(),
        elevation = 0.dp,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onOpenDrawer) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = stringResource(R.string.menu),
                    tint = MaterialTheme.colors.onSurface,
                )
            }

            Text(
                text = stringResource(id = R.string.saved_entries),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onSurface,
            )
        }
    }
}