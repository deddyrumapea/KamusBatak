package com.romnan.kamusbatak.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.romnan.kamusbatak.domain.model.Entry


@Composable
fun EntryItem(
    entry: Entry,
    modifier: Modifier = Modifier,
    onClick: (Entry) -> Unit,
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colors.surface)
            .fillMaxWidth()
            .clickable { onClick(entry) },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(entry.headword.orEmpty())
                    }

                    append(" ")

                    withStyle(
                        SpanStyle(
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                        ),
                    ) {
                        append(entry.shortPosLabel.orEmpty())
                    }

                    append(" ")

                    append(entry.definitions.orEmpty())
                },
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }

        Divider(modifier = Modifier.padding(horizontal = 8.dp))
    }
}