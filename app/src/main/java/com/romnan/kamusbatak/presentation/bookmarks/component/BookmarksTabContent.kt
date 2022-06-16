package com.romnan.kamusbatak.presentation.bookmarks.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.romnan.kamusbatak.R
import com.romnan.kamusbatak.domain.model.Entry
import com.romnan.kamusbatak.presentation.components.EntryItem

@Composable
fun BookmarksTabContent(
    modifier: Modifier = Modifier,
    entries: List<Entry>,
    onItemClick: (entry: Entry) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        if (entries.isEmpty()) Text(text = stringResource(R.string.no_entries))
        else LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(entries.size) { i ->
                EntryItem(
                    entry = entries[i],
                    onClick = onItemClick,
                )
            }
        }
    }
}