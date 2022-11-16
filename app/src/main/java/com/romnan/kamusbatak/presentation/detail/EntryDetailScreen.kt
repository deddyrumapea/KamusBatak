package com.romnan.kamusbatak.presentation.detail

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.romnan.kamusbatak.R
import com.romnan.kamusbatak.domain.util.Constants
import com.romnan.kamusbatak.presentation.detail.component.EntryDetailTopBar
import com.romnan.kamusbatak.presentation.theme.spacing

@Destination(
    route = Destination.ROOT_NAV_GRAPH_ROUTE,
    deepLinks = [DeepLink(uriPattern = "app://com.romnan.kamusbatak/detail/{entryId}")],
)
@Composable
fun EntryDetailScreen(
    entryId: Int,
    navigator: DestinationsNavigator,
    viewModel: EntryDetailViewModel = hiltViewModel(),
) {
    remember { viewModel.onReceiveEntryId(entryId) }

    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()

    val state = viewModel.state.value

    Scaffold(scaffoldState = scaffoldState, topBar = {
        EntryDetailTopBar(
            isBookmarked = state.entry.isBookmarked,
            onBackClick = { navigator.navigateUp() },
            onShareClick = {
                val shareText = context.getString(
                    R.string.format_share_entry_message, state.entry.word, state.entry.meaning
                )

                Intent(Intent.ACTION_SEND).let { intent ->
                    intent.type = Constants.INTENT_TYPE_PLAIN_TEXT
                    intent.putExtra(Intent.EXTRA_TEXT, shareText)
                    context.startActivity(intent)
                }
            },
            onToggleBookmark = { viewModel.onToggleBookmark() },
        )
    }) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.medium)
                    .background(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colors.surface,
                    )
                    .padding(MaterialTheme.spacing.medium)
            ) {
                Text(
                    text = state.entry.word,
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onSurface,
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

                Text(
                    text = state.entry.meaning,
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onSurface,
                )
            }
        }
    }
}