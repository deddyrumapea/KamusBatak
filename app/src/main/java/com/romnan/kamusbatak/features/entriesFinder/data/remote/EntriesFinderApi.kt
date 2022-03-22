package com.romnan.kamusbatak.features.entriesFinder.data.remote

import com.romnan.kamusbatak.features.entriesFinder.data.remote.dto.RemoteEntryDto
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface EntriesFinderApi {
    @GET("entry")
    suspend fun getEntries(@QueryMap params: Map<String, String>): List<RemoteEntryDto>
}