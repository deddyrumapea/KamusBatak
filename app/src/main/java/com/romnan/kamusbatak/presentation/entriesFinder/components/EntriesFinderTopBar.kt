package com.romnan.kamusbatak.presentation.entriesFinder.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.romnan.kamusbatak.R

@Composable
fun EntriesFinderTopBar(
    modifier: Modifier = Modifier,
    targetLangName: String,
    sourceLangName: String,
    isOptionsMenuVisible: Boolean,
    onOpenDrawer: () -> Unit,
    onOpenOptionsMenu: () -> Unit,
    onCloseOptionsMenu: () -> Unit,
    onSwapClick: () -> Unit,
    onShareAppClick: () -> Unit,
    onSendSuggestionsClick: () -> Unit,
) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.surface,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onOpenDrawer) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = stringResource(R.string.menu),
                    tint = MaterialTheme.colors.onSurface
                )
            }

            AnimatedContent(
                targetState = sourceLangName,
                modifier = Modifier.weight(1f),
                transitionSpec = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left
                    ) togetherWith slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right
                    )
                },
                label = "sourceLangName",
            ) {
                Text(
                    text = it,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.onSurface
                )
            }

            IconButton(
                onClick = onSwapClick,
                modifier = Modifier
                    .background(color = MaterialTheme.colors.primary, shape = CircleShape)
                    .size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.SwapHoriz,
                    contentDescription = stringResource(id = R.string.cd_swap_languages),
                    tint = MaterialTheme.colors.onPrimary,
                )
            }

            AnimatedContent(
                targetState = targetLangName,
                modifier = Modifier.weight(1f),
                transitionSpec = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right
                    ) togetherWith slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left
                    )
                },
                label = "targetLangName",
            ) {
                Text(
                    text = it,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.onSurface
                )
            }

            IconButton(onClick = onOpenOptionsMenu) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(R.string.cd_options),
                    tint = MaterialTheme.colors.onSurface
                )
            }
        }

        Box {
            DropdownMenu(
                expanded = isOptionsMenuVisible,
                onDismissRequest = onCloseOptionsMenu,
            ) {
                DropdownMenuItem(onClick = onShareAppClick) {
                    Text(text = stringResource(R.string.share_this_app))
                }

                DropdownMenuItem(onClick = onSendSuggestionsClick) {
                    Text(text = stringResource(R.string.send_suggestions))
                }
            }
        }
    }
}