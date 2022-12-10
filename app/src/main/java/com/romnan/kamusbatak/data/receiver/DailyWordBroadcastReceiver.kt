package com.romnan.kamusbatak.data.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.romnan.kamusbatak.domain.helper.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import logcat.logcat
import javax.inject.Inject

@AndroidEntryPoint
class DailyWordBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onReceive(p0: Context?, p1: Intent?) {
        logcat { "daily word broadcast received" }
        MainScope().launch {
            notificationHelper.showDailyWordNotif()
        }
    }

    companion object {
        private const val PENDING_INTENT_REQUEST_CODE = 314

        fun getPendingIntent(appContext: Context): PendingIntent {
            return PendingIntent.getBroadcast(
                appContext,
                PENDING_INTENT_REQUEST_CODE,
                Intent(appContext, DailyWordBroadcastReceiver::class.java),
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                } else {
                    PendingIntent.FLAG_UPDATE_CURRENT
                }
            )
        }
    }
}