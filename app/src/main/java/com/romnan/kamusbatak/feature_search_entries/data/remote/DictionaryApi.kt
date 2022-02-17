package com.romnan.kamusbatak.feature_search_entries.data.remote

import com.romnan.kamusbatak.feature_search_entries.data.remote.dto.EntryDto
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface DictionaryApi {
    @GET("entry")
    suspend fun getEntries(@QueryMap params: Map<String, String>): List<EntryDto>
}