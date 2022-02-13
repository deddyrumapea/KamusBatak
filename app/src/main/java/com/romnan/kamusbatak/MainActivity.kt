package com.romnan.kamusbatak

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.romnan.kamusbatak.feature_dictionary.presentation.EntryItem
import com.romnan.kamusbatak.feature_dictionary.presentation.SearchEntriesEvent
import com.romnan.kamusbatak.feature_dictionary.presentation.SearchEntriesViewModel
import com.romnan.kamusbatak.ui.theme.KamusBatakTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KamusBatakTheme {
                // TODO: add progress bar when loading

                val viewModel: SearchEntriesViewModel = hiltViewModel()
                val state = viewModel.state.value
                val scaffoldState = rememberScaffoldState()

                LaunchedEffect(key1 = true) {
                    viewModel.eventFlow.collectLatest { event ->
                        when (event) {
                            is SearchEntriesViewModel.UIEvent.ShowSnackbar -> {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    message = event.message
                                )
                            }
                        }
                    }
                }

                Scaffold(scaffoldState = scaffoldState) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        TopAppBar(
                            title = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = state.sourceLanguage.fullName,
                                        style = MaterialTheme.typography.subtitle1
                                            .copy(fontWeight = FontWeight.Bold)
                                    )
                                    IconButton(onClick = {
                                        viewModel.onEvent(SearchEntriesEvent.LanguagesSwap)
                                    }) {
                                        Icon(
                                            painterResource(id = R.drawable.ic_outline_sync_alt_24),
                                            getString(R.string.swap_languages)
                                        )
                                    }
                                    Text(
                                        text = state.targetLanguage.fullName,
                                        style = MaterialTheme.typography.subtitle1
                                            .copy(fontWeight = FontWeight.Bold)
                                    )
                                }
                            },
                            backgroundColor = MaterialTheme.colors.primary
                        )

                        TextField(
                            value = viewModel.searchQuery.value,
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text(text = getString(R.string.enter_words_here)) },
                            onValueChange = {
                                viewModel.onEvent(SearchEntriesEvent.QueryChange(it))
                            }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(state.entries.size) { i ->
                                if (i > 0) Spacer(modifier = Modifier.height(8.dp))
                                EntryItem(entry = state.entries[i])
                                Spacer(modifier = Modifier.height(8.dp))
                                if (i < state.entries.size - 1) Divider()
                            }
                        }
                    }
                }
                if (state.isLoading) CircularProgressIndicator()
            }
        }
    }
}