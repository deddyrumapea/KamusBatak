package com.romnan.kamusbatak.presentation.detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.romnan.kamusbatak.R

@Composable
fun EntryDetailTopBar(
    modifier: Modifier = Modifier,
    isBookmarked: Boolean,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    onToggleBookmark: () -> Unit,
) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.primary,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(id = R.string.cd_nav_back),
                    tint = MaterialTheme.colors.onPrimary,
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = onToggleBookmark) {
                Icon(
                    imageVector =
                    if (isBookmarked) Icons.Default.Bookmark
                    else Icons.Default.BookmarkBorder,
                    contentDescription = stringResource(
                        if (isBookmarked) R.string.unsave_entry
                        else R.string.save_entry
                    ),
                    tint = MaterialTheme.colors.onPrimary,
                )
            }

            IconButton(onClick = onShareClick) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = stringResource(R.string.cd_share),
                    tint = MaterialTheme.colors.onPrimary,
                )
            }
        }
    }
}