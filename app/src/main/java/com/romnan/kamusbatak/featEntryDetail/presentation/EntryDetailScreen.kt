package com.romnan.kamusbatak.featEntryDetail.presentation

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.romnan.kamusbatak.R
import com.romnan.kamusbatak.core.presentation.model.EntryParcelable
import com.romnan.kamusbatak.core.presentation.theme.spacing
import com.romnan.kamusbatak.core.util.Constants

@Destination
@Composable
fun EntryDetailScreen(
    navigator: DestinationsNavigator,
    entry: EntryParcelable
) {
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()

    Scaffold(scaffoldState = scaffoldState) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.surface)
        ) {
            TopAppBar(backgroundColor = MaterialTheme.colors.primary) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = { navigator.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(id = R.string.cd_nav_back),
                            tint = MaterialTheme.colors.onPrimary
                        )
                    }

                    Row {
                        IconButton(onClick = {
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
                        }) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = stringResource(R.string.cd_share),
                                tint = MaterialTheme.colors.onPrimary,
                            )
                        }
                    }
                }
            }

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