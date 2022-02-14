package com.romnan.kamusbatak.feature_search_entries.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.romnan.kamusbatak.feature_search_entries.domain.model.Entry

@Entity
data class EntryEntity(
    @PrimaryKey val id: Int? = null,
    val btkWord: String,
    val indWord: String,
    val dialect: String?,
    val phonetic: String?,
    val example: String?
) {
    fun toEntry() = Entry(
        btkWord = btkWord,
        indWord = indWord,
        dialect = dialect,
        phonetic = phonetic,
        example = example
    )
}