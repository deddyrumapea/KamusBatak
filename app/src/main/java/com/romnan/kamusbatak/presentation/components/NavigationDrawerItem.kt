package com.romnan.kamusbatak.presentation.components

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
import com.romnan.kamusbatak.presentation.theme.spacing

@Composable
fun NavigationDrawerItem(
    modifier: Modifier = Modifier,
    isSelected: @Composable () -> Boolean = { false },
    icon: () -> ImageVector,
    label: @Composable () -> String,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(topEndPercent = 100, bottomEndPercent = 100))
            .background(
                if (isSelected()) MaterialTheme.colors.secondaryVariant
                else MaterialTheme.colors.surface
            )
            .clickable { onClick() }
            .padding(
                vertical = MaterialTheme.spacing.medium,
                horizontal = MaterialTheme.spacing.large,
            ),
    ) {
        Icon(
            imageVector = icon(),
            contentDescription = null,
            tint = if (isSelected()) MaterialTheme.colors.onSecondary
            else MaterialTheme.colors.onSurface
        )

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

        Text(
            text = label(),
            color = if (isSelected()) MaterialTheme.colors.onSecondary
            else MaterialTheme.colors.onSurface,
            style = MaterialTheme.typography.subtitle1,
        )
    }
}