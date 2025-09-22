package com.romnan.kamusbatak.domain.model

import com.romnan.kamusbatak.presentation.util.uposToLabel
import com.romnan.kamusbatak.presentation.util.uposToShortLabel

data class Entry(
    val id: Int? = null,
    val createdAt: String? = null,
    val definitions: String? = null,
    val deletedAt: String? = null,
    val headword: String? = null,
    val partOfSpeech: String? = null,
    val semanticCategories: String? = null,
    val sourceDialect: String? = null,
    val sourceLang: String? = null,
    val targetLang: String? = null,
    val updatedAt: String? = null,
    val bookmarkedAt: String? = null,
) {
    val isBookmarked: Boolean
        get() = bookmarkedAt != null

    val shortPosLabel: String?
        get() = uposToShortLabel[partOfSpeech]

    val posLabel: String?
        get() = uposToLabel[partOfSpeech]
}