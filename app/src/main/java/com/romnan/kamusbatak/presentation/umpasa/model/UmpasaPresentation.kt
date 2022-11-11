package com.romnan.kamusbatak.presentation.umpasa.model

import com.romnan.kamusbatak.domain.model.Umpasa
import com.romnan.kamusbatak.domain.model.UmpasaCategory

data class UmpasaPresentation(
    val category: UmpasaCategory,
    val content: String,
    val meaning: String,
    val isMeaningShown: Boolean,
)

fun Umpasa.toPresentation(
    isMeaningShown: Boolean,
) = UmpasaPresentation(
    category = this.category,
    content = this.content,
    meaning = this.meaning,
    isMeaningShown = isMeaningShown,
)