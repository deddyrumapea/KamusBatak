package com.romnan.kamusbatak.data.retrofit.dto

import com.google.gson.annotations.SerializedName
import com.romnan.kamusbatak.data.room.entity.EntryEntity

data class EntryDto(
    @SerializedName("created_at") val createdAt: String? = null,
    @SerializedName("definitions") val definitions: String? = null,
    @SerializedName("deleted_at") val deletedAt: String? = null,
    @SerializedName("headword") val headword: String? = null,
    @SerializedName("id") val id: Int? = null,
    @SerializedName("part_of_speech") val partOfSpeech: String? = null,
    @SerializedName("semantic_categories") val semanticCategories: String? = null,
    @SerializedName("source_dialect") val sourceDialect: String? = null,
    @SerializedName("source_lang") val sourceLang: String? = null,
    @SerializedName("target_lang") val targetLang: String? = null,
    @SerializedName("updated_at") val updatedAt: String? = null,
) {
    fun toEntryEntity() = EntryEntity(
        createdAt = createdAt,
        definitions = definitions,
        deletedAt = deletedAt,
        headword = headword,
        id = id,
        partOfSpeech = partOfSpeech,
        semanticCategories = semanticCategories,
        sourceDialect = sourceDialect,
        sourceLang = sourceLang,
        targetLang = targetLang,
        updatedAt = updatedAt,
    )
}