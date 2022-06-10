package com.romnan.kamusbatak.featPreferences.presentation

import com.romnan.kamusbatak.R
import com.romnan.kamusbatak.core.util.Constants
import com.romnan.kamusbatak.core.util.UIText
import java.text.SimpleDateFormat
import java.util.*

data class OfflineSupportScreenState(
    val isUpdating: Boolean,
    val lastUpdatedTimeMillis: Long?,
) {
    val lastUpdatedUiText: UIText
        get() = when (lastUpdatedTimeMillis) {
            null -> UIText.StringResource(R.string.data_never_downloaded)
            else -> {
                val sdf = SimpleDateFormat(Constants.PATTERN_DATE, Locale.getDefault())
                val date = Date().apply { this.time = lastUpdatedTimeMillis }
                UIText.DynamicString(sdf.format(date))
            }
        }

    companion object {
        val defaultValue = OfflineSupportScreenState(
            isUpdating = false,
            lastUpdatedTimeMillis = null,
        )
    }
}