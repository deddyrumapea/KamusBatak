package com.romnan.kamusbatak.feature_search_entries.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.romnan.kamusbatak.feature_search_entries.data.local.entity.EntryEntity

data class EntryDto(
    @SerializedName("id") val id: Int?,
    @SerializedName("src_lang") val srcLang: String?,
    @SerializedName("word") val word: String?,
    @SerializedName("meaning") val meaning: String?
) {
    fun toEntryEntity() = EntryEntity(
        id = id,
        srcLang = srcLang ?: "",
        word = word ?: "",
        meaning = meaning ?: ""
    )
}