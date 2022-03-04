package com.romnan.kamusbatak.core.presentation.model

import android.os.Parcelable
import com.romnan.kamusbatak.core.domain.model.Entry
import kotlinx.parcelize.Parcelize

@Parcelize
data class EntryParcelable(
    val srcLang: String,
    val word: String,
    val meaning: String,
) : Parcelable {
    constructor(entry: Entry) : this(
        srcLang = entry.srcLang,
        word = entry.word,
        meaning = entry.meaning
    )
}