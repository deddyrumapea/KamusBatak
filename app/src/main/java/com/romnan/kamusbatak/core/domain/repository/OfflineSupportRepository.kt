package com.romnan.kamusbatak.core.domain.repository

import com.romnan.kamusbatak.core.util.SimpleResource
import kotlinx.coroutines.flow.Flow

interface OfflineSupportRepository {
    fun downloadUpdate(): Flow<SimpleResource>
    suspend fun isOfflineFullySupported(): Boolean
    fun getLastUpdatedAt(): Flow<Long?>
}