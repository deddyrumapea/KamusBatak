package com.romnan.kamusbatak.presentation.partuturan

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
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
import com.romnan.kamusbatak.presentation.components.DefaultTopBar
import com.romnan.kamusbatak.presentation.theme.spacing
import com.romnan.kamusbatak.presentation.util.UIEvent
import com.romnan.kamusbatak.presentation.util.asString
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Destination
@Composable
fun PartuturanScreen(
    viewModel: PartuturanViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    parentScaffoldState: ScaffoldState,
) {
    val scaffoldState = rememberScaffoldState()

    val scope = rememberCoroutineScope()

    val context = LocalContext.current

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

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            DefaultTopBar(
                title = { stringResource(R.string.partuturan) },
                onOpenDrawer = { scope.launch { parentScaffoldState.drawerState.open() } },
            )
        },
    ) { scaffoldPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
        ) {
            item { Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium)) }

            items(viewModel.partuturansList.size) { i ->
                Card(
                    shape = RoundedCornerShape(8.dp),
                    onClick = { viewModel.onClickShowDescription(i) },
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
                                text = viewModel.partuturansList[i].title,
                                modifier = Modifier.weight(1f),
                            )

                            IconButton(onClick = { viewModel.onClickShowDescription(i) }) {
                                Icon(
                                    imageVector = if (viewModel.partuturansList[i].isDescriptionShown) Icons.Default.ExpandLess
                                    else Icons.Default.ExpandMore,
                                    contentDescription = stringResource(R.string.see_meaning),
                                )
                            }
                        }

                        AnimatedVisibility(visible = viewModel.partuturansList[i].isDescriptionShown) {
                            Column(
                                modifier = Modifier
                                    .padding(top = MaterialTheme.spacing.medium)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colors.secondary.copy(alpha = 0.2f))
                                    .padding(MaterialTheme.spacing.medium),
                            ) {
                                viewModel.partuturansList[i].descriptions.forEachIndexed { j: Int, description: String ->
                                    Row(modifier = Modifier.fillMaxWidth(),) {
                                        Text(text = (j + 1).toString())
                                        Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                                        Text(text = description)
                                    }

                                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                                }
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