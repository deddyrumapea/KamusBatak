package com.romnan.kamusbatak.data.repository

import android.app.AlarmManager
import android.content.Context
import com.romnan.kamusbatak.data.datastore.AppPreferencesManager
import com.romnan.kamusbatak.data.receiver.DailyWordBroadcastReceiver
import com.romnan.kamusbatak.domain.model.ThemeMode
import com.romnan.kamusbatak.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import logcat.logcat

class PreferencesRepositoryImpl(
    private val appContext: Context,
    private val appPreferencesManager: AppPreferencesManager,
) : PreferencesRepository {

    override val themeMode: Flow<ThemeMode>
        get() = appPreferencesManager.dataStore.data.map {
            try {
                ThemeMode.valueOf(it.themeModeName)
            } catch (e: IllegalArgumentException) {
                ThemeMode.System
            }
        }

    override val dailyWordTime: Flow<Long?>
        get() = appPreferencesManager.dataStore.data.map { it.dailyWordTimeInMillis }

    override suspend fun setThemeMode(themeMode: ThemeMode) {
        appPreferencesManager.dataStore.updateData {
            it.copy(themeModeName = themeMode.name)
        }
    }

    override suspend fun setDailyWordTime(
        timeInMillis: Long?,
    ) {
        appPreferencesManager.dataStore.updateData {
            if (timeInMillis != null) setDailyWordAlarm(timeInMillis)
            else cancelDailyWordAlarm()

            it.copy(dailyWordTimeInMillis = timeInMillis)
        }
    }

    private fun setDailyWordAlarm(timeInMillis: Long) {
        val alarmManager = appContext.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        alarmManager?.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            timeInMillis,
            AlarmManager.INTERVAL_DAY,
            DailyWordBroadcastReceiver.getPendingIntent(appContext = appContext),
        )
        logcat { "daily word time alarm set: $timeInMillis" }
    }

    private fun cancelDailyWordAlarm() {
        val pendingIntent = DailyWordBroadcastReceiver.getPendingIntent(appContext = appContext)
        pendingIntent.cancel()

        val alarmManager = appContext.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        alarmManager?.cancel(pendingIntent)
        logcat { "daily word alarm canceled" }
    }
}