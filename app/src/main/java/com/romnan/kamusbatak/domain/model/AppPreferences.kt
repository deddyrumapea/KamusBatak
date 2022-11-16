package com.romnan.kamusbatak.domain.model

import com.romnan.kamusbatak.R
import com.romnan.kamusbatak.domain.util.UIText
import kotlinx.serialization.Serializable

@Serializable
data class AppPreferences(
    val localDbLastUpdatedAt: Long?,
    val themeModeName: String,
    val dailyWordTimeInMillis: Long?,
) {
    companion object {
        val defaultValue = AppPreferences(
            localDbLastUpdatedAt = null,
            themeModeName = ThemeMode.System.name,
            dailyWordTimeInMillis = null,
        )
    }
}

enum class ThemeMode(val readableName: UIText) {
    System(readableName = UIText.StringResource(R.string.theme_system)),
    Light(readableName = UIText.StringResource(R.string.theme_light)),
    Dark(readableName = UIText.StringResource(R.string.theme_dark)),
}