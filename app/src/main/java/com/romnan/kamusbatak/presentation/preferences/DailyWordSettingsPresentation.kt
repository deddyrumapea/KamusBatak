package com.romnan.kamusbatak.presentation.preferences

import com.romnan.kamusbatak.domain.util.UIText
import com.romnan.kamusbatak.presentation.constants.TimeConstants
import java.text.SimpleDateFormat
import java.util.*

class DailyWordSettingsPresentation(
    notifTimeInMillis: Long?
) {
    val isActivated: Boolean
    val calendar: Calendar
    val readableTime: UIText?

    init {
        isActivated = notifTimeInMillis != null
        calendar = notifTimeInMillis?.let { millis ->
            Calendar.getInstance().apply { timeInMillis = millis }
        } ?: TimeConstants.DEFAULT_DAILY_NOTIF_TIME
        readableTime = UIText.DynamicString(
            SimpleDateFormat(
                TimeConstants.TWENTY_FOUR_HOUR_FORMAT, Locale.getDefault()
            ).format(calendar.time)
        )
    }

    companion object {
        val defaultValue = DailyWordSettingsPresentation(
            notifTimeInMillis = null,
        )
    }
}