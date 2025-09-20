package com.romnan.kamusbatak.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.romnan.kamusbatak.domain.model.Entry

@Entity(tableName = "dict_entries")
data class EntryEntity(
    @PrimaryKey val id: Int? = null,
    val createdAt: String?,
    val definition: String?,
    val deletedAt: String?,
    val headword: String?,
    val partOfSpeech: String?,
    val semanticCategories: String?,
    val sourceDialect: String?,
    val sourceLang: String?,
    val targetLang: String?,
    val updatedAt: String?,
    val bookmarkedAt: String? = null,
) {
    fun toEntry() = Entry(
        id = id,
        createdAt = createdAt,
        definition = definition,
        deletedAt = deletedAt,
        headword = headword,
        partOfSpeech = partOfSpeech,
        semanticCategories = semanticCategories,
        sourceDialect = sourceDialect,
        sourceLang = sourceLang,
        targetLang = targetLang,
        updatedAt = updatedAt,
        bookmarkedAt = bookmarkedAt,
    )
}