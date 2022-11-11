package com.romnan.kamusbatak.domain.model

import com.romnan.kamusbatak.R
import com.romnan.kamusbatak.domain.util.UIText

enum class UmpasaCategory(
    val readableName: UIText,
) {
    ALL(readableName = UIText.StringResource(R.string.umpasa_cat_all)),

    WEDDING(readableName = UIText.StringResource(R.string.umpasa_cat_wedding)),

    FUNERAL(readableName = UIText.StringResource(R.string.umpasa_cat_funeral)),
}