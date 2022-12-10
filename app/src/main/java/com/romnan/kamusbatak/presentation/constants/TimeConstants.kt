package com.romnan.kamusbatak.presentation.constants

import java.util.*

object TimeConstants {
    const val TWENTY_FOUR_HOUR_FORMAT = "HH:mm"

    val DEFAULT_DAILY_NOTIF_TIME: Calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 21)
        set(Calendar.MINUTE, 0)
    }
}