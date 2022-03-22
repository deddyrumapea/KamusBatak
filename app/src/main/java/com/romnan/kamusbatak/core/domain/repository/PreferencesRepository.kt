package com.romnan.kamusbatak.core.domain.repository

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    fun getLastOfflineSupportUpdatedAt(): Flow<Long?>
    suspend fun setLastOfflineSupportUpdatedAt(timeInMillis: Long)
}