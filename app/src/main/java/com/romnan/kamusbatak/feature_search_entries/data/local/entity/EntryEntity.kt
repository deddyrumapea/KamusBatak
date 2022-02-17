package com.romnan.kamusbatak.feature_search_entries.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.romnan.kamusbatak.feature_search_entries.domain.model.Entry

@Entity
data class EntryEntity(
    @PrimaryKey val id: Int? = null,
    val srcLang: String,
    val word: String,
    val meaning: String
) {
    fun toEntry() = Entry(
        srcLang = srcLang,
        word = word,
        meaning = meaning
    )
}