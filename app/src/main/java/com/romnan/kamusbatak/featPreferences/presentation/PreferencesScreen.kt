package com.romnan.kamusbatak.featPreferences.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Sync
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.romnan.kamusbatak.R
import com.romnan.kamusbatak.core.presentation.theme.spacing
import com.romnan.kamusbatak.core.presentation.util.UIEvent
import com.romnan.kamusbatak.core.presentation.util.asString
import kotlinx.coroutines.flow.collectLatest

@Destination
@Composable
fun PreferencesScreen(
    navigator: DestinationsNavigator,
    viewModel: PreferencesViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UIEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.uiText.asString(context)
                    )
                }
            }
        }
    }

    Scaffold(scaffoldState = scaffoldState) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(backgroundColor = MaterialTheme.colors.primary) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { navigator.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(id = R.string.cd_nav_back),
                            tint = MaterialTheme.colors.onPrimary,
                        )
                    }

                    Text(
                        text = stringResource(R.string.preferences),
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.colors.onPrimary,
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(MaterialTheme.colors.surface)
                    .clickable { viewModel.onEvent(PreferencesEvent.DownloadUpdate) }
                    .padding(MaterialTheme.spacing.medium)
                    .fillMaxWidth()
            ) {
                Column {
                    if (state.isUpdating) {
                        CircularProgressIndicator(modifier = Modifier.size(36.dp))
                    } else {
                        Icon(
                            imageVector = Icons.Default.Sync,
                            contentDescription = stringResource(R.string.update),
                            modifier = Modifier.size(36.dp),
                            tint = MaterialTheme.colors.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = when {
                            state.lastUpdatedTimeMillis == null ->
                                stringResource(R.string.download_dictionary_data)
                            else -> stringResource(R.string.update_dictionary_data)
                        },
                        style = MaterialTheme.typography.subtitle1,
                        color = MaterialTheme.colors.onSurface
                    )

                    Text(
                        text = stringResource(R.string.update_description),
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface,
                    )

                    Text(
                        text = context.getString(
                            R.string.format_last_updated,
                            state.lastUpdatedUiText.asString()
                        ),
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.onSurface,
                    )
                }
            }

            Divider(modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium))
        }
    }
}