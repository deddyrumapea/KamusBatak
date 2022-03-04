package com.romnan.kamusbatak.feature_entry_detail

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.romnan.kamusbatak.core.presentation.model.EntryParcelable

@Destination
@Composable
fun EntryDetailScreen(
    entry: EntryParcelable
) {
    Text("Detail screen $entry")
}