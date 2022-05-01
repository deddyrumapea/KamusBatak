package com.romnan.kamusbatak.core.domain.model

import com.romnan.kamusbatak.R
import com.romnan.kamusbatak.core.util.UIText

sealed class Language {
    abstract val displayName: UIText
    abstract val codeName: String

    object Ind : Language() {
        override val displayName: UIText = UIText.StringResource(R.string.indonesia)
        override val codeName: String = "ind"
    }

    object Btk : Language() {
        override val displayName: UIText = UIText.StringResource(R.string.batak)
        override val codeName: String = "btk"
    }
}
