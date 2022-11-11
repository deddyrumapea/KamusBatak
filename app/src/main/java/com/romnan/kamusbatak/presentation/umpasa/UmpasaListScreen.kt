package com.romnan.kamusbatak.presentation.umpasa

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.romnan.kamusbatak.R
import com.romnan.kamusbatak.presentation.theme.spacing
import com.romnan.kamusbatak.presentation.util.UIEvent
import com.romnan.kamusbatak.presentation.util.asString
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterialApi::class)
@Destination
@Composable
fun UmpasaListScreen(
    catName: String,
    navigator: DestinationsNavigator,
    viewModel: UmpasaViewModel = hiltViewModel(),
) {
    remember { viewModel.onReceivedCategoryName(catName) }

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

    Scaffold(scaffoldState = scaffoldState, topBar = {
        TopAppBar(
            backgroundColor = MaterialTheme.colors.primary,
            modifier = Modifier.fillMaxWidth(),
            elevation = 0.dp,
        ) {
            IconButton(onClick = { navigator.navigateUp() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.cd_nav_back),
                    tint = contentColorFor(backgroundColor = MaterialTheme.colors.primary),
                )
            }

            Text(
                text = context.getString(
                    R.string.format_umpasas_list_title,
                    viewModel.category.readableName.asString(),
                ),
                style = MaterialTheme.typography.h6,
                color = contentColorFor(backgroundColor = MaterialTheme.colors.primary),
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }) { scaffoldPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding),
        ) {
            item { Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium)) }

            items(viewModel.umpasasList.size) { i ->
                Card(
                    shape = RoundedCornerShape(8.dp),
                    onClick = { viewModel.onClickSeeMeaning(i) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.spacing.medium),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(MaterialTheme.spacing.medium)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(
                                text = viewModel.umpasasList[i].content,
                                modifier = Modifier.weight(1f),
                            )

                            IconButton(onClick = { viewModel.onClickSeeMeaning(i) }) {
                                Icon(
                                    imageVector = if (viewModel.umpasasList[i].isMeaningShown) Icons.Default.ExpandLess
                                    else Icons.Default.ExpandMore,
                                    contentDescription = stringResource(R.string.see_meaning),
                                )
                            }
                        }

                        AnimatedVisibility(visible = viewModel.umpasasList[i].isMeaningShown) {
                            Column(
                                modifier = Modifier
                                    .padding(top = MaterialTheme.spacing.medium)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colors.secondary.copy(alpha = 0.2f))
                                    .padding(MaterialTheme.spacing.medium),
                            ) {
                                Text(text = viewModel.umpasasList[i].meaning)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            }

            item { Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium)) }
        }
    }
}