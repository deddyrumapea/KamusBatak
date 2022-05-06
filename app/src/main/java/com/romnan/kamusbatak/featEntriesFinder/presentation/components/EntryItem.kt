package com.romnan.kamusbatak.featEntriesFinder.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.romnan.kamusbatak.core.domain.model.Entry


@Composable
fun EntryItem(
    entry: Entry,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .fillMaxWidth()
    ) {

        val spanStyles = listOf(
            AnnotatedString.Range(
                item = SpanStyle(fontWeight = FontWeight.Bold),
                start = 0,
                end = entry.word.length
            )
        )
        Text(
            text = AnnotatedString(
                text = "${entry.word}  ${entry.meaning}",
                spanStyles = spanStyles
            ),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
    }
}