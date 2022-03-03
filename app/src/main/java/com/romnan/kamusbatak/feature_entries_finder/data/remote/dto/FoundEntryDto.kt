package com.romnan.kamusbatak.feature_entries_finder.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.romnan.kamusbatak.core.data.local.entity.CachedEntryEntity

data class FoundEntryDto(
    @SerializedName("id") val id: Int?,
    @SerializedName("src_lang") val srcLang: String?,
    @SerializedName("word") val word: String?,
    @SerializedName("meaning") val meaning: String?
) {
    fun toCachedEntryEntity() = CachedEntryEntity(
        id = id,
        srcLang = srcLang ?: "",
        word = word ?: "",
        meaning = meaning ?: ""
    )
}