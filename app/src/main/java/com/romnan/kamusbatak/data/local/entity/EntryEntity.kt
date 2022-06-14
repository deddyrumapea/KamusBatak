package com.romnan.kamusbatak.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.romnan.kamusbatak.domain.model.Entry

@Entity
data class EntryEntity(
    @PrimaryKey val id: Int? = null,
    val srcLang: String,
    val word: String,
    val meaning: String,
    val updatedAt: String,
    val isBookmarked: Boolean,
) {
    fun toEntry() = Entry(
        id = id,
        word = word,
        meaning = meaning,
        isBookmarked = isBookmarked,
    )
}