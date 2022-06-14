package com.romnan.kamusbatak.presentation.preferences

import com.romnan.kamusbatak.R
import com.romnan.kamusbatak.domain.util.Constants
import com.romnan.kamusbatak.domain.util.UIText
import java.text.SimpleDateFormat
import java.util.*

data class PreferencesScreenState(
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
        val defaultValue = PreferencesScreenState(
            isUpdating = false,
            lastUpdatedTimeMillis = null,
        )
    }
}