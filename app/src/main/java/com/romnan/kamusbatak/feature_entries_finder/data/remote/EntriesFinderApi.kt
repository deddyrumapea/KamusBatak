package com.romnan.kamusbatak.feature_entries_finder.data.remote

import com.romnan.kamusbatak.feature_entries_finder.data.remote.dto.FoundEntryDto
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface EntriesFinderApi {
    @GET("entry")
    suspend fun getEntries(@QueryMap params: Map<String, String>): List<FoundEntryDto>
}