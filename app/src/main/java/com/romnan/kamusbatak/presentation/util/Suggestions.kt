package com.romnan.kamusbatak.presentation.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.net.toUri
import com.romnan.kamusbatak.BuildConfig
import com.romnan.kamusbatak.R
import com.romnan.kamusbatak.domain.model.Entry
import com.romnan.kamusbatak.presentation.quizGame.model.QuizItemPresentation
import logcat.asLog
import logcat.logcat

private const val SUGGESTION_EMAIL_ADDRESS = "romnanstudio@gmail.com"

fun launchSendSuggestionIntent(
    context: Context,
    entry: Entry
) {
    val body = context.getString(
        R.string.entry_suggestion_email_body,
        entry.headword.orEmpty(),
        entry.definitions.orEmpty(),
        entry.id.toString(),
        Build.VERSION.SDK_INT.toString(),
        Build.MANUFACTURER,
        Build.MODEL,
        BuildConfig.VERSION_CODE.toString(),
    )

    val intent = Intent(Intent.ACTION_SEND).apply {
        data = "mailto:$SUGGESTION_EMAIL_ADDRESS".toUri()
        putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.suggestions_for_kamus_batak))
        putExtra(Intent.EXTRA_TEXT, body)
    }

    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        logcat("launchSendSuggestionIntent") { e.asLog() }
    }
}

fun launchSendSuggestionIntent(
    context: Context,
) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        data = "mailto:$SUGGESTION_EMAIL_ADDRESS".toUri()
        putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.suggestions_for_kamus_batak))
        putExtra(Intent.EXTRA_TEXT, context.getString(R.string.type_your_suggestions_here))
    }

    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        logcat("launchSendSuggestionIntent") { e.asLog() }
    }
}

fun launchSendSuggestionIntent(
    context: Context,
    quizItem: QuizItemPresentation,
) {
    val alphabet = ('A'..'Z').toList()
    val options = quizItem.options.mapIndexed { i: Int, option: String ->
        "(${alphabet[i % alphabet.size]}) $option${if (i == quizItem.correctOptionIdx) " ✅" else ""}"
    }.joinToString("\n")

    val body = context.getString(
        R.string.quiz_item_suggestion_email_body,
        quizItem.question,
        options,
        quizItem.entryId.toString(),
        Build.VERSION.SDK_INT.toString(),
        Build.MANUFACTURER,
        Build.MODEL,
        BuildConfig.VERSION_CODE.toString(),
    ).trimIndent()

    val intent = Intent(Intent.ACTION_SEND).apply {
        data = "mailto:$SUGGESTION_EMAIL_ADDRESS".toUri()
        putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.suggestions_for_kamus_batak))
        putExtra(Intent.EXTRA_TEXT, body)
    }

    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        logcat("launchSendSuggestionIntent") { e.asLog() }
    }
}