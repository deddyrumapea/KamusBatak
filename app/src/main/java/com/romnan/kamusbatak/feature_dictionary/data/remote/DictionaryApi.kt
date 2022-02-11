package com.romnan.kamusbatak.feature_dictionary.data.remote

import com.romnan.kamusbatak.feature_dictionary.data.remote.dto.EntryDto
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface DictionaryApi {

    companion object {
        const val BASE_URL = "https://xuodmwstqpsmiabqemoy.supabase.co/rest/v1/"
    }

    @GET("entry")
    suspend fun searchWithBatakKeyword(
        @Query("btk_word") keyword: String,
        @Query("select") select: String = "*"
    ): List<EntryDto>
}