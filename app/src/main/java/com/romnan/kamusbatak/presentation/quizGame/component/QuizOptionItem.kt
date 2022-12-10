package com.romnan.kamusbatak.presentation.quizGame.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.romnan.kamusbatak.presentation.theme.spacing

@Composable
fun QuizOptionItem(
    char: () -> Char,
    text: () -> String,
    isSelected: () -> Boolean,
    isCorrect: () -> Boolean,
    isShowingAnswer: () -> Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
            .background(
                when {
                    isShowingAnswer() && isCorrect() -> MaterialTheme.colors.secondaryVariant
                    isShowingAnswer() && isSelected() -> MaterialTheme.colors.error
                    isSelected() -> MaterialTheme.colors.secondary
                    else -> MaterialTheme.colors.surface
                }.copy(alpha = 0.2f)
            )
            .border(
                width = 2.dp,
                color = when {
                    isShowingAnswer() && isCorrect() -> MaterialTheme.colors.secondaryVariant
                    isShowingAnswer() && isSelected() -> MaterialTheme.colors.error
                    isSelected() -> MaterialTheme.colors.secondary
                    else -> MaterialTheme.colors.onSurface.copy(alpha = 0.1f)
                },
                shape = RoundedCornerShape(8.dp),
            )
            .padding(MaterialTheme.spacing.medium),
    ) {
        val charBg = when {
            isShowingAnswer() && isCorrect() -> MaterialTheme.colors.secondaryVariant
            isShowingAnswer() && isSelected() -> MaterialTheme.colors.error
            isSelected() -> MaterialTheme.colors.secondary
            else -> MaterialTheme.colors.onSurface.copy(alpha = 0.1f)
        }

        Text(
            text = char().toString(),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = when {
                isShowingAnswer() && isCorrect() -> MaterialTheme.colors.onPrimary
                isShowingAnswer() && isSelected() -> MaterialTheme.colors.onPrimary
                isSelected() -> MaterialTheme.colors.onPrimary
                else -> MaterialTheme.colors.onSurface.copy(alpha = 0.4f)
            },
            modifier = Modifier
                .padding(MaterialTheme.spacing.medium)
                .drawBehind {
                    drawCircle(
                        color = charBg,
                        radius = this.size.maxDimension,
                    )
                },
        )

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

        Text(
            text = text(),
            color = when {
                isShowingAnswer() && isCorrect() -> MaterialTheme.colors.secondaryVariant
                isShowingAnswer() && isSelected() -> MaterialTheme.colors.error
                isSelected() -> MaterialTheme.colors.secondary
                else -> MaterialTheme.colors.onSurface
            },
        )
    }
}