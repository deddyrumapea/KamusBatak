package com.romnan.kamusbatak.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.romnan.kamusbatak.core.domain.model.Entry

@Entity
data class EntryEntity(
    @PrimaryKey val id: Int? = null,
    val srcLang: String,
    val word: String,
    val meaning: String,
    val updatedAt: String
) {
    fun toEntry() = Entry(
        srcLang = srcLang,
        word = word,
        meaning = meaning
    )
}