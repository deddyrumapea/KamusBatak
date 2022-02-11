package com.romnan.kamusbatak.feature_dictionary.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.romnan.kamusbatak.feature_dictionary.domain.model.Entry

@Composable
fun EntryItem(entry: Entry, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = entry.btkWord,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(text = entry.phonetic ?: "", fontWeight = FontWeight.Light)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = entry.dialect ?: "")
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = entry.indWord)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = entry.example ?: "")
    }
}