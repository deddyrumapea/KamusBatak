package com.romnan.kamusbatak.feature_dictionary.data.remote

import com.romnan.kamusbatak.feature_dictionary.data.remote.dto.EntryDto
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface DictionaryApi {

    companion object {
        // TODO: hide this base URL
        const val BASE_URL = "https://xuodmwstqpsmiabqemoy.supabase.co/rest/v1/"
    }

    @GET("entry")
    suspend fun getEntries(
        @QueryMap params: Map<String, String>
    ): List<EntryDto>
}