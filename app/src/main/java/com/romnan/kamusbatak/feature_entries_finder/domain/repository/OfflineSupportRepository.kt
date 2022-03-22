package com.romnan.kamusbatak.feature_entries_finder.domain.repository

import com.romnan.kamusbatak.core.util.Resource
import com.romnan.kamusbatak.core.util.SimpleResource
import kotlinx.coroutines.flow.Flow

interface OfflineSupportRepository {
    fun downloadUpdate(): Flow<SimpleResource>
    suspend fun isOfflineFullySupported(): Boolean
}