package com.romnan.kamusbatak.featEntryDetail.presentation

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.romnan.kamusbatak.R
import com.romnan.kamusbatak.core.presentation.model.EntryParcelable
import com.romnan.kamusbatak.core.presentation.theme.spacing
import com.romnan.kamusbatak.core.util.Constants
import com.romnan.kamusbatak.featEntryDetail.presentation.component.EntryDetailTopBar

@Destination
@Composable
fun EntryDetailScreen(
    navigator: DestinationsNavigator,
    entry: EntryParcelable,
) {
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            EntryDetailTopBar(
                onBackClick = { navigator.navigateUp() },
                onShareClick = {
                    val text = context.getString(
                        R.string.format_share_message,
                        entry.word,
                        entry.meaning
                    )

                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = Constants.INTENT_TYPE_PLAIN_TEXT
                        putExtra(Intent.EXTRA_TEXT, text)
                    }

                    context.startActivity(intent)
                }
            )
        }
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
                .background(MaterialTheme.colors.surface)
        ) {
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            Text(
                text = entry.word,
                style = MaterialTheme.typography.h6
                    .copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium),
                color = MaterialTheme.colors.onSurface,
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            Text(
                text = entry.meaning,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium),
                color = MaterialTheme.colors.onSurface,
            )
        }
    }
}