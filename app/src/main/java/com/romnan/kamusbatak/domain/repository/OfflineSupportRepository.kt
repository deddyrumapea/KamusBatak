package com.romnan.kamusbatak.domain.repository

import com.romnan.kamusbatak.domain.util.SimpleResource
import kotlinx.coroutines.flow.Flow

interface OfflineSupportRepository {
    fun downloadUpdate(): Flow<SimpleResource>
    suspend fun isFullySupported(): Boolean
    val lastUpdatedAt: Flow<Long?>
}