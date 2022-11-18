package com.romnan.kamusbatak.presentation.suggestions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.romnan.kamusbatak.R
import com.romnan.kamusbatak.domain.model.Language
import com.romnan.kamusbatak.presentation.components.RoundedEndsButton
import com.romnan.kamusbatak.presentation.suggestions.components.LanguageOptionItem
import com.romnan.kamusbatak.presentation.theme.spacing
import com.romnan.kamusbatak.presentation.util.UIEvent
import com.romnan.kamusbatak.presentation.util.asString
import kotlinx.coroutines.flow.collectLatest

@Destination
@Composable
fun SuggestionsScreen(
    entryId: Int?,
    navigator: DestinationsNavigator,
    viewModel: SuggestionsViewModel = hiltViewModel(),
) {
    remember { entryId?.let { viewModel.onReceivedEntryId(it) } }

    val scaffoldState = rememberScaffoldState()

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
                    text = stringResource(R.string.suggestions),
                    style = MaterialTheme.typography.h6,
                    color = contentColorFor(backgroundColor = MaterialTheme.colors.primary),
                )

                Spacer(modifier = Modifier.weight(1f))
            }
        },
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
                .verticalScroll(rememberScrollState()),
        ) {
            Card(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(MaterialTheme.spacing.medium),
            ) {
                Column(
                    modifier = Modifier
                        .padding(MaterialTheme.spacing.medium)
                        .fillMaxWidth(),
                ) {
                    if (viewModel.entryId == null) {
                        Text(text = stringResource(R.string.source_language))

                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

                        LazyRow(modifier = Modifier.fillMaxWidth()) {
                            val languages = Language.values()
                            items(languages.size) { i ->
                                LanguageOptionItem(
                                    languageName = { languages[i].displayName.asString() },
                                    isSelected = { viewModel.srcLang == languages[i] },
                                    onClick = { viewModel.onChangeSrcLang(languages[i]) },
                                )

                                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                            }
                        }

                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                    }

                    TextField(
                        value = viewModel.word,
                        onValueChange = { viewModel.onChangeWord(it) },
                        placeholder = { Text(text = stringResource(R.string.word)) },
                        shape = RoundedCornerShape(8.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        ),
                        enabled = !viewModel.isLoadingSubmit,
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                    TextField(
                        value = viewModel.meaning,
                        onValueChange = { viewModel.onChangeMeaning(it) },
                        placeholder = { Text(text = stringResource(R.string.meaning)) },
                        shape = RoundedCornerShape(8.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            errorIndicatorColor = Color.Transparent,
                        ),
                        enabled = !viewModel.isLoadingSubmit,
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                    RoundedEndsButton(
                        onClick = { viewModel.onClickSubmit() },
                        enabled = !viewModel.isLoadingSubmit,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = stringResource(
                                if (viewModel.isLoadingSubmit) R.string.submitting
                                else R.string.submit
                            ),
                            fontWeight = FontWeight.SemiBold,
                        )

                        Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = null,
                        )
                    }
                }
            }
        }
    }
}