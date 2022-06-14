package com.romnan.kamusbatak.data.remote

import com.romnan.kamusbatak.data.remote.dto.RemoteEntryDto
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface CoreApi {
    @GET("entry")
    suspend fun getEntries(@QueryMap params: Map<String, String>): List<RemoteEntryDto>
}