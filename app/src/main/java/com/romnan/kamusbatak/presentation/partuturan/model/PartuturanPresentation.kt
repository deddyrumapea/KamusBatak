package com.romnan.kamusbatak.presentation.partuturan.model

import com.romnan.kamusbatak.domain.model.Partuturan

data class PartuturanPresentation(
    val title: String,
    val descriptions: List<String>,
    val isDescriptionShown: Boolean,
)

fun Partuturan.toPresentation(
    isDescriptionShown: Boolean,
) = PartuturanPresentation(
    title = this.title,
    descriptions = this.descriptions,
    isDescriptionShown = isDescriptionShown,
)