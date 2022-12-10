package com.romnan.kamusbatak.domain.model

import com.romnan.kamusbatak.R
import com.romnan.kamusbatak.domain.util.UIText

enum class ThemeMode(val readableName: UIText) {
    System(readableName = UIText.StringResource(R.string.theme_system)),

    Light(readableName = UIText.StringResource(R.string.theme_light)),

    Dark(readableName = UIText.StringResource(R.string.theme_dark)),
}