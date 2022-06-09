package com.romnan.kamusbatak.core.presentation.model

import android.os.Parcelable
import com.romnan.kamusbatak.core.domain.model.Entry
import kotlinx.parcelize.Parcelize

@Parcelize
data class EntryParcelable(
    val srcLang: String,
    val word: String,
    val meaning: String,
) : Parcelable

fun Entry.toParcelable() = EntryParcelable(
    srcLang = this.srcLang,
    word = this.word,
    meaning = this.meaning,
)