package com.romnan.kamusbatak.featEntryDetail.presentation

import android.content.Intent
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
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.romnan.kamusbatak.R
import com.romnan.kamusbatak.core.presentation.model.EntryParcelable
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
        Column(modifier = Modifier.fillMaxSize()) {
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
                            contentDescription = stringResource(id = R.string.cd_nav_back)
                        )
                    }

                    Row {
                        // TODO: implement favorite feature
//                        IconButton(onClick = {  }) {
//                            Icon(
//                                imageVector = Icons.Default.FavoriteBorder,
//                                contentDescription = stringResource(R.string.add_to_favorite)
//                            )
//                        }
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
                                contentDescription = stringResource(R.string.cd_share)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = entry.word,
                style = MaterialTheme.typography.h6
                    .copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = entry.meaning,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}