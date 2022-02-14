package com.romnan.kamusbatak.feature_search_entries.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.romnan.kamusbatak.feature_search_entries.domain.model.Entry

@Composable
fun EntryItem(entry: Entry, modifier: Modifier = Modifier) {
    // TODO: improve item design
    Column(modifier = modifier.padding(16.dp)) {
        entry.dialect?.let { Text(text = it, style = MaterialTheme.typography.caption) }

        Text(
            text = entry.btkWord,
            fontSize = 24.sp,
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.onSurface,
            fontWeight = FontWeight.Bold
        )

        entry.phonetic?.let {
            Text(
                text = "/$it/",
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.onSurface,
                fontWeight = FontWeight.Light
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(text = entry.indWord, style = MaterialTheme.typography.body1)

        entry.example?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = it, style = MaterialTheme.typography.body1)
        }
    }
}