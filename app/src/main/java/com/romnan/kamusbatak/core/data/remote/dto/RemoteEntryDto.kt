package com.romnan.kamusbatak.core.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.romnan.kamusbatak.core.data.local.entity.EntryEntity
import com.romnan.kamusbatak.core.domain.model.Entry

data class RemoteEntryDto(
    @SerializedName(Field.ID) val id: Int?,
    @SerializedName(Field.SRC_LANG) val srcLang: String?,
    @SerializedName(Field.WORD) val word: String?,
    @SerializedName(Field.MEANING) val meaning: String?,
    @SerializedName(Field.UPDATED_AT) val updatedAt: String?
) {
    fun toEntryEntity() = EntryEntity(
        id = id,
        srcLang = srcLang ?: "",
        word = word ?: "",
        meaning = meaning ?: "",
        updatedAt = updatedAt ?: "",
        isBookmarked = false,
    )

    object Field {
        const val ID = "id"
        const val SRC_LANG = "src_lang"
        const val WORD = "word"
        const val MEANING = "meaning"
        const val UPDATED_AT = "updated_at"
    }
}