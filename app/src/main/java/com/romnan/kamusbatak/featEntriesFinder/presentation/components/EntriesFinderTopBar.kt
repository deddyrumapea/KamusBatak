package com.romnan.kamusbatak.featEntriesFinder.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
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

@OptIn(ExperimentalAnimationApi::class)
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
                targetState = targetLangName,
                modifier = Modifier.weight(1f),
                transitionSpec = {
                    slideIntoContainer(
                        towards = AnimatedContentScope.SlideDirection.Left
                    ) with slideOutOfContainer(
                        towards = AnimatedContentScope.SlideDirection.Right
                    )
                }
            ) {
                Text(
                    text = sourceLangName,
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
                        towards = AnimatedContentScope.SlideDirection.Right
                    ) with slideOutOfContainer(
                        towards = AnimatedContentScope.SlideDirection.Left
                    )
                }
            ) {
                Text(
                    text = targetLangName,
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
            }
        }
    }
}