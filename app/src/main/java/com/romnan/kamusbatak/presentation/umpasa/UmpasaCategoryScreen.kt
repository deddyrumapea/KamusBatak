package com.romnan.kamusbatak.presentation.umpasa

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.romnan.kamusbatak.R
import com.romnan.kamusbatak.domain.model.UmpasaCategory
import com.romnan.kamusbatak.presentation.components.DefaultTopBar
import com.romnan.kamusbatak.presentation.destinations.UmpasaListScreenDestination
import com.romnan.kamusbatak.presentation.theme.spacing
import com.romnan.kamusbatak.presentation.util.asString
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Destination
@Composable
fun UmpasaCategoryScreen(
    navigator: DestinationsNavigator,
    parentScaffoldState: ScaffoldState,
) {
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            DefaultTopBar(
                title = { stringResource(id = R.string.umpasa) },
                onOpenDrawer = { scope.launch { parentScaffoldState.drawerState.open() } },
            )
        },
        scaffoldState = rememberScaffoldState(),
    ) { scaffoldPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding),
        ) {
            item { Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium)) }

            items(UmpasaCategory.values().size) { i ->
                Card(
                    onClick = {
                        navigator.navigate(
                            UmpasaListScreenDestination(catName = UmpasaCategory.values()[i].name)
                        )
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .padding(horizontal = MaterialTheme.spacing.medium)
                        .fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(MaterialTheme.spacing.medium)
                    ) {
                        Text(text = UmpasaCategory.values()[i].readableName.asString())
                    }
                }

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            }

            item { Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium)) }
        }
    }
}