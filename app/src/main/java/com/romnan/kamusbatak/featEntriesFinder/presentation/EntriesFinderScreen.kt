package com.romnan.kamusbatak.featEntriesFinder.presentation

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.romnan.kamusbatak.R
import com.romnan.kamusbatak.core.presentation.model.toParcelable
import com.romnan.kamusbatak.core.presentation.theme.spacing
import com.romnan.kamusbatak.core.presentation.util.UIEvent
import com.romnan.kamusbatak.core.presentation.util.asString
import com.romnan.kamusbatak.core.util.Constants
import com.romnan.kamusbatak.destinations.EntryDetailScreenDestination
import com.romnan.kamusbatak.featEntriesFinder.presentation.components.EntriesFinderTopBar
import com.romnan.kamusbatak.featEntriesFinder.presentation.components.EntryItem
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@RootNavGraph(start = true)
@Destination
@Composable
fun EntriesFinderScreen(
    viewModel: EntriesFinderViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    parentScaffoldState: ScaffoldState,
) {
    val state = viewModel.state.value
    val context = LocalContext.current

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

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
            EntriesFinderTopBar(
                targetLangName = state.targetLanguage.displayName.asString(),
                sourceLangName = state.sourceLanguage.displayName.asString(),
                isOptionsMenuVisible = state.isOptionsMenuVisible,
                onOpenDrawer = { scope.launch { parentScaffoldState.drawerState.open() } },
                onOpenOptionsMenu = { viewModel.onOpenOptionsMenu() },
                onCloseOptionsMenu = { viewModel.onCloseOptionsMenu() },
                onSwapClick = { viewModel.onSwapLanguages() },
                onShareAppClick = {
                    viewModel.onCloseOptionsMenu()
                    val shareText = context.getString(R.string.download_kamus_batak)
                    Intent(Intent.ACTION_SEND).let { intent ->
                        intent.type = Constants.INTENT_TYPE_PLAIN_TEXT
                        intent.putExtra(Intent.EXTRA_TEXT, shareText)
                        context.startActivity(intent)
                    }
                }
            )
        },
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
                .background(MaterialTheme.colors.background)
        ) {
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            val bottomCorRad by animateDpAsState(if (state.isLoadingEntries) 0.dp else 8.dp)
            TextField(
                singleLine = true,
                value = viewModel.searchQuery.value,
                placeholder = { Text(text = stringResource(R.string.ph_enter_words_here)) },
                onValueChange = { viewModel.onQueryChange(it) },
                trailingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(8.dp, 8.dp, bottomCorRad, bottomCorRad),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    backgroundColor = MaterialTheme.colors.surface,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.small)
            )

            AnimatedVisibility(visible = state.isLoadingEntries) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.spacing.small)
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(viewModel.entries.value.size) { i ->
                    EntryItem(
                        entry = viewModel.entries.value[i],
                        onClick = {
                            navigator.navigate(EntryDetailScreenDestination(it.toParcelable()))
                        }
                    )
                }
            }
        }
    }
}