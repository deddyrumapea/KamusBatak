package com.romnan.kamusbatak.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import com.romnan.kamusbatak.R
import com.romnan.kamusbatak.domain.util.UIText
import com.romnan.kamusbatak.presentation.destinations.*
import com.romnan.kamusbatak.presentation.theme.spacing
import com.romnan.kamusbatak.presentation.util.asString

@Composable
fun NavigationDrawerContent(
    currentDestination: @Composable () -> Destination,
    onItemClick: (direction: DirectionDestinationSpec) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colors.surface)
            .fillMaxSize()
            .padding(
                top = MaterialTheme.spacing.medium,
                end = MaterialTheme.spacing.medium,
                bottom = MaterialTheme.spacing.medium,
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = MaterialTheme.spacing.medium)
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                color = MaterialTheme.colors.onSurface,
                style = MaterialTheme.typography.h6,
            )
        }

        Divider(
            modifier = Modifier.padding(
                start = MaterialTheme.spacing.medium,
                top = MaterialTheme.spacing.medium,
                bottom = MaterialTheme.spacing.medium,
            )
        )

        LazyColumn {
            items(DrawerItem.values().size) { i ->
                NavigationDrawerItem(
                    icon = { DrawerItem.values()[i].icon },
                    label = { DrawerItem.values()[i].label.asString() },
                    isSelected = { currentDestination() == DrawerItem.values()[i].direction },
                    onClick = { onItemClick(DrawerItem.values()[i].direction) },
                )
            }
        }
    }
}

private enum class DrawerItem(
    val direction: DirectionDestinationSpec,
    val icon: ImageVector,
    val label: UIText,
) {
    EntriesFinder(
        direction = EntriesFinderScreenDestination,
        icon = Icons.Default.Search,
        label = UIText.StringResource(R.string.dictionary),
    ),
    QuizGames(
        direction = QuizGamesScreenDestination,
        icon = Icons.Default.SportsEsports,
        label = UIText.StringResource(R.string.quiz_games),
    ),
    Bookmarks(
        direction = BookmarksScreenDestination,
        icon = Icons.Default.CollectionsBookmark,
        label = UIText.StringResource(R.string.saved_entries),
    ),
    Partuturan(
        direction = PartuturanScreenDestination,
        icon = Icons.Default.Group,
        label = UIText.StringResource(R.string.partuturan),
    ),
    Umpasa(
        direction = UmpasaCategoryScreenDestination,
        icon = Icons.Default.SpeakerNotes,
        label = UIText.StringResource(R.string.umpasa),
    ),
    Preferences(
        direction = PreferencesScreenDestination,
        icon = Icons.Default.Settings,
        label = UIText.StringResource(R.string.preferences),
    ),
}