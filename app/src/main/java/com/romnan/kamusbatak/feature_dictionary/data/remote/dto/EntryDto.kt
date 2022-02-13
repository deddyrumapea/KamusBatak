package com.romnan.kamusbatak.feature_dictionary.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.romnan.kamusbatak.feature_dictionary.data.local.entity.EntryEntity

data class EntryDto(
    @SerializedName("id") val id: Int,
    @SerializedName("btk_word") val btkWord: String,
    @SerializedName("ind_word") val indWord: String,
    @SerializedName("dialect") val dialect: String?,
    @SerializedName("phonetic") val phonetic: String?,
    @SerializedName("example") val example: String?
) {
    fun toEntryEntity() = EntryEntity(
        id = id,
        btkWord = btkWord,
        indWord = indWord,
        dialect = dialect,
        phonetic = phonetic,
        example = example
    )
}