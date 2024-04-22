package com.romnan.kamusbatak.data.retrofit

import com.romnan.kamusbatak.data.retrofit.dto.EntryDto
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface EntryApi {
    @GET("entry")
    suspend fun getEntries(
        @QueryMap params: Map<String, String>,
    ): List<EntryDto>
}