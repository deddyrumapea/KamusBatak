package com.romnan.kamusbatak.feature_search_entries.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.romnan.kamusbatak.feature_search_entries.domain.model.Entry

@Composable
fun EntryItem(entry: Entry, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            entry.dialect?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onSurface
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = entry.btkWord,
                color = MaterialTheme.colors.onSurface,
                style = MaterialTheme.typography.subtitle1
                    .copy(fontWeight = FontWeight.SemiBold)
            )

            Spacer(modifier = Modifier.width(8.dp))

            entry.phonetic?.let {
                Text(
                    text = "/$it/",
                    color = MaterialTheme.colors.onSurface,
                    style = MaterialTheme.typography.subtitle1
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = entry.indWord,
            color = MaterialTheme.colors.onSurface,
            style = MaterialTheme.typography.subtitle1
        )

        entry.example?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = it, fontStyle = FontStyle.Italic)
        }
    }
}