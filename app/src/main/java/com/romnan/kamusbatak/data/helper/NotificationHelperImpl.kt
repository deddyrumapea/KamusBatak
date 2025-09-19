package com.romnan.kamusbatak.data.helper

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.romnan.kamusbatak.R
import com.romnan.kamusbatak.domain.helper.NotificationHelper
import com.romnan.kamusbatak.domain.repository.DictionaryRepository
import logcat.logcat

class NotificationHelperImpl(
    private val appContext: Context,
    private val dictionaryRepository: DictionaryRepository,
) : NotificationHelper {

    private val notificationManager = NotificationManagerCompat.from(appContext)

    private val pendingIntentFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    } else {
        PendingIntent.FLAG_UPDATE_CURRENT
    }

    init {
        createNotificationChannels()
    }

    override suspend fun showDailyWordNotif() {
        val entry = dictionaryRepository.getRandomEntry()

        if (entry == null) {
            logcat { "showDailyNotif(): random entry is null" }
            return
        }

        val pendingIntent = PendingIntent.getActivity(
            appContext,
            0,
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("app://com.romnan.kamusbatak/detail/${entry.id}")
            },
            pendingIntentFlags
        )

        val notification =
            NotificationCompat.Builder(appContext, NotificationConstants.CHANNEL_ID_DAILY_WORD)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentIntent(pendingIntent)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle(
                    appContext.getString(
                        R.string.notif_title_word_of_the_day,
                        entry.headword.orEmpty()
                    )
                )
                .setContentText(entry.definition.orEmpty())
                .setAutoCancel(true)
                .build()

        if (ActivityCompat.checkSelfPermission(
                appContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        notificationManager.notify(NotificationConstants.NOTIFICATION_ID_DAILY_WORD, notification)
    }

    private fun createNotificationChannels() {
        val channels = listOf(
            NotificationChannelCompat.Builder(
                NotificationConstants.CHANNEL_ID_DAILY_WORD,
                NotificationManagerCompat.IMPORTANCE_HIGH
            ).setSound(
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build()
            ).setName(appContext.getString(R.string.notif_channel_name_daily_notif))
                .setDescription(appContext.getString(R.string.notif_channel_desc_daily_notif))
                .setLightsEnabled(true).setVibrationEnabled(true).build(),
        )

        notificationManager.createNotificationChannelsCompat(channels)
    }
}