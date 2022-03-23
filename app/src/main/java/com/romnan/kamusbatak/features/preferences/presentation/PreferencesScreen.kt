package com.romnan.kamusbatak.features.preferences.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Sync
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.romnan.kamusbatak.R

@Destination
@Composable
fun PreferencesScreen(
    navigator: DestinationsNavigator,
    viewModel: PreferencesViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()

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
                            contentDescription = stringResource(id = R.string.back)
                        )
                    }

                    Text(
                        text = stringResource(R.string.preferences),
                        style = MaterialTheme.typography.h6
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable { viewModel.onEvent(PreferencesEvent.DownloadUpdate) }
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Column {
                    if (state.isUpdating) {
                        CircularProgressIndicator(modifier = Modifier.size(36.dp))
                    } else {
                        Icon(
                            imageVector = Icons.Default.Sync,
                            contentDescription = stringResource(R.string.update),
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = when {
                            state.lastUpdated.isNullOrEmpty() ->
                                stringResource(R.string.download_dictionary_data)

                            else -> stringResource(R.string.update_dictionary_data)
                        },
                        style = MaterialTheme.typography.subtitle1,
                    )

                    Text(
                        text = stringResource(R.string.update_description),
                        style = MaterialTheme.typography.body2
                    )

                    Text(
                        text = when {
                            state.lastUpdated.isNullOrEmpty() ->
                                stringResource(R.string.data_never_downloaded)

                            else -> context.getString(R.string.last_updated, state.lastUpdated)
                        },
                        style = MaterialTheme.typography.caption
                    )
                }
            }

            Divider(modifier = Modifier.padding(horizontal = 16.dp))
        }
    }
}