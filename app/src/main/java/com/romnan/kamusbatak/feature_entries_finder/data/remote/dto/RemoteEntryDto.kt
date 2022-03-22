package com.romnan.kamusbatak.feature_entries_finder.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.romnan.kamusbatak.core.data.local.entity.CachedEntryEntity

data class RemoteEntryDto(
    @SerializedName(Field.ID) val id: Int?,
    @SerializedName(Field.SRC_LANG) val srcLang: String?,
    @SerializedName(Field.WORD) val word: String?,
    @SerializedName(Field.MEANING) val meaning: String?,
    @SerializedName(Field.UPDATED_AT) val updatedAt: String?
) {
    fun toCachedEntryEntity() = CachedEntryEntity(
        id = id,
        srcLang = srcLang ?: "",
        word = word ?: "",
        meaning = meaning ?: "",
        updatedAt = updatedAt ?: ""
    )

    object Field {
        const val ID = "id"
        const val SRC_LANG = "src_lang"
        const val WORD = "word"
        const val MEANING = "meaning"
        const val UPDATED_AT = "updated_at"
    }
}