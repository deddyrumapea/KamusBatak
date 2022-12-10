package com.romnan.kamusbatak.presentation.suggestions.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.romnan.kamusbatak.presentation.theme.spacing

@Composable
fun LanguageOptionItem(
    languageName: @Composable () -> String,
    isSelected: () -> Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .then(
                if (!isSelected()) Modifier.border(
                    width = 1.dp,
                    color = MaterialTheme.colors.secondary,
                    shape = RoundedCornerShape(8.dp),
                ) else Modifier.background(MaterialTheme.colors.secondary)
            )
            .padding(MaterialTheme.spacing.small),
    ) {
        Text(
            text = languageName(),
            color = if (isSelected()) MaterialTheme.colors.onSecondary
            else MaterialTheme.colors.secondary,
            fontWeight = FontWeight.SemiBold,
        )
    }
}