package com.romnan.kamusbatak.domain.model

import com.romnan.kamusbatak.R
import com.romnan.kamusbatak.domain.util.UIText

enum class Language(
    val displayName: UIText,
    val codeName: String,
) {
    IND(
        displayName = UIText.StringResource(R.string.indonesia),
        codeName = "ind",
    ),

    BTK(
        displayName = UIText.StringResource(R.string.batak),
        codeName = "btk",
    ),
}
