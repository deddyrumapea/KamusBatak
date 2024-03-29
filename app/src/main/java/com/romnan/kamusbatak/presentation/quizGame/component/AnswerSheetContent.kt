package com.romnan.kamusbatak.presentation.quizGame.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.romnan.kamusbatak.R
import com.romnan.kamusbatak.presentation.components.RoundedEndsButton
import com.romnan.kamusbatak.presentation.theme.spacing

@Composable
fun AnswerSheetContent(
    isAnswerCorrect: () -> Boolean,
    correctAnswer: () -> String,
    onClickNext: () -> Unit,
    onClickReport: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
            .background(
                if (isAnswerCorrect()) MaterialTheme.colors.secondaryVariant.copy(alpha = 0.2f)
                else MaterialTheme.colors.error.copy(alpha = 0.2f)
            )
            .padding(MaterialTheme.spacing.medium),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = stringResource(
                    if (isAnswerCorrect()) R.string.correct
                    else R.string.incorrect
                ),
                color = if (isAnswerCorrect()) MaterialTheme.colors.secondaryVariant
                else MaterialTheme.colors.error,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
            )

            Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

            IconButton(onClick = { onClickReport() }) {
                Icon(
                    imageVector = Icons.Default.Flag,
                    contentDescription = stringResource(R.string.cd_report_answer),
                    tint = if (isAnswerCorrect()) MaterialTheme.colors.secondaryVariant
                    else MaterialTheme.colors.error,
                )
            }
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

        Text(
            text = stringResource(R.string.correct_answer_is),
            color = if (isAnswerCorrect()) MaterialTheme.colors.secondaryVariant
            else MaterialTheme.colors.error,
            fontWeight = FontWeight.SemiBold,
        )

        Text(
            text = correctAnswer(),
            color = if (isAnswerCorrect()) MaterialTheme.colors.secondaryVariant
            else MaterialTheme.colors.error,
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

        RoundedEndsButton(
            onClick = { onClickNext() },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (isAnswerCorrect()) MaterialTheme.colors.secondaryVariant
                else MaterialTheme.colors.error
            ),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = stringResource(id = R.string.next),
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}