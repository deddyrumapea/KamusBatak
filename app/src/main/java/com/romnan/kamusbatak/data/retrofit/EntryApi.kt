package com.romnan.kamusbatak.data.retrofit

import com.romnan.kamusbatak.data.retrofit.dto.EntryDto
import retrofit2.http.*

interface EntryApi {
    @GET("entry")
    suspend fun getEntries(
        @QueryMap params: Map<String, String>,
    ): List<EntryDto>

    @POST("old_entry_suggestions")
    @FormUrlEncoded
    suspend fun postOldEntrySuggestion(
        @Field("entry_id") entryId: Int,
        @Field("word") word: String,
        @Field("meaning") meaning: String,
    ): Unit

    @POST("new_entry_suggestions")
    @FormUrlEncoded
    suspend fun postNewEntrySuggestion(
        @Field("src_lang") srcLang: String,
        @Field("word") word: String,
        @Field("meaning") meaning: String,
    ): Unit
}