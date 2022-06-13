package com.romnan.kamusbatak.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun NavigationDrawerItem(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(topEndPercent = 100, bottomEndPercent = 100))
            .background(
                if (selected) MaterialTheme.colors.secondaryVariant
                else MaterialTheme.colors.surface
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (selected) MaterialTheme.colors.onSecondary else MaterialTheme.colors.onSurface
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            color = if (selected) MaterialTheme.colors.onSecondary else MaterialTheme.colors.onSurface,
            style = MaterialTheme.typography.subtitle1
        )
    }
}