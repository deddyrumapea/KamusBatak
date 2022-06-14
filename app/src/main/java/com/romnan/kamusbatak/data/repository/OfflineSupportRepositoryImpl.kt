package com.romnan.kamusbatak.data.repository

import com.romnan.kamusbatak.R
import com.romnan.kamusbatak.data.local.CoreDao
import com.romnan.kamusbatak.data.preferences.CorePreferences
import com.romnan.kamusbatak.data.remote.CoreApi
import com.romnan.kamusbatak.data.remote.dto.RemoteEntryDto
import com.romnan.kamusbatak.domain.model.LocalDictionary
import com.romnan.kamusbatak.domain.repository.OfflineSupportRepository
import com.romnan.kamusbatak.domain.util.Resource
import com.romnan.kamusbatak.domain.util.SimpleResource
import com.romnan.kamusbatak.domain.util.UIText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import logcat.logcat
import okio.IOException
import retrofit2.HttpException

class OfflineSupportRepositoryImpl(
    private val coreApi: CoreApi,
    private val coreDao: CoreDao,
    private val corePref: CorePreferences
) : OfflineSupportRepository {

    override fun downloadUpdate(): Flow<SimpleResource> = flow {
        emit(Resource.Loading())

        try {
            val latestEntryUpdatedAt = coreDao.getLatestEntryUpdatedAt()
            val params = when {
                isFullySupported() && !latestEntryUpdatedAt.isNullOrBlank() ->
                    mapOf(RemoteEntryDto.Field.UPDATED_AT to "gt.$latestEntryUpdatedAt")
                else -> emptyMap()
            }
            val remoteEntries = coreApi.getEntries(params = params)
            coreDao.insertEntries(remoteEntries.map { it.toEntryEntity() })
            setLastUpdatedAt(System.currentTimeMillis())
            logcat { "downloadUpdate: ${remoteEntries.size} new entries inserted to local cache" }

            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            logcat { "downloadUpdate: ${e::class.java.simpleName}" }
            when (e) {
                is HttpException -> UIText.StringResource(R.string.em_http_exception)
                is IOException -> UIText.StringResource(R.string.em_io_exception)
                else -> UIText.StringResource(R.string.em_unknown)
            }.let { emit(Resource.Error(uiText = it)) }
        }
    }

    override suspend fun isFullySupported(): Boolean {
        return lastUpdatedAt.firstOrNull() != null
    }

    override val lastUpdatedAt: Flow<Long?>
        get() = corePref.dataStore.data.map { it.localDictionary.lastUpdated }

    private suspend fun setLastUpdatedAt(currentTimeMillis: Long) {
        corePref.dataStore.updateData {
            it.copy(localDictionary = LocalDictionary(lastUpdated = currentTimeMillis))
        }
    }
}