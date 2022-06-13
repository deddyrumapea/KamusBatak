package com.romnan.kamusbatak.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.DrawerState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.ramcosta.composedestinations.navigation.navigateTo
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import com.romnan.kamusbatak.NavGraphs
import com.romnan.kamusbatak.R
import com.romnan.kamusbatak.appCurrentDestinationAsState
import com.romnan.kamusbatak.core.presentation.theme.spacing
import com.romnan.kamusbatak.core.presentation.util.asString
import com.romnan.kamusbatak.core.util.UIText
import com.romnan.kamusbatak.destinations.Destination
import com.romnan.kamusbatak.destinations.EntriesFinderScreenDestination
import com.romnan.kamusbatak.destinations.PreferencesScreenDestination
import com.romnan.kamusbatak.startAppDestination
import kotlinx.coroutines.launch

@Composable
fun NavigationDrawerContent(
    modifier: Modifier = Modifier,
    navController: NavController,
    drawerState: DrawerState,
) {
    val currentDestination: Destination = navController.appCurrentDestinationAsState().value
        ?: NavGraphs.root.startAppDestination

    val scope = rememberCoroutineScope()

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
                val item = DrawerItem.values()[i]
                NavigationDrawerItem(
                    icon = item.icon,
                    label = item.label.asString(),
                    selected = currentDestination == item.direction,
                    onClick = {
                        scope.launch { drawerState.close() }
                        if (item == DrawerItem.EntriesFinder) navController.popBackStack()
                        navController.navigateTo(item.direction) { launchSingleTop = true }
                    },
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
    Preferences(
        direction = PreferencesScreenDestination,
        icon = Icons.Default.Settings,
        label = UIText.StringResource(R.string.preferences),
    ),
}