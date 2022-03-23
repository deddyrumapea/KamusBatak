package com.romnan.kamusbatak.features.entriesFinder.data.repository

import com.romnan.kamusbatak.core.data.local.CoreDao
import com.romnan.kamusbatak.features.entriesFinder.domain.repository.OfflineSupportRepository
import com.romnan.kamusbatak.core.domain.repository.PreferencesRepository
import com.romnan.kamusbatak.core.util.Resource
import com.romnan.kamusbatak.core.util.SimpleResource
import com.romnan.kamusbatak.features.entriesFinder.data.remote.EntriesFinderApi
import com.romnan.kamusbatak.features.entriesFinder.data.remote.dto.RemoteEntryDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException

class OfflineSupportRepositoryImpl(
    private val api: EntriesFinderApi,
    private val coreDao: CoreDao,
    private val prefRepository: PreferencesRepository
) : OfflineSupportRepository {

    override fun downloadUpdate(): Flow<SimpleResource> = flow {
        emit(Resource.Loading())

        try {
            val latestEntryUpdatedAt = coreDao.getLatestEntryUpdatedAt()

            val params = when {
                latestEntryUpdatedAt.isNullOrEmpty() -> emptyMap()
                else -> mapOf(RemoteEntryDto.Field.UPDATED_AT to "gt.$latestEntryUpdatedAt")
            }

            val remoteEntries = api.getEntries(params = params)

            coreDao.insertCachedEntries(remoteEntries.map { it.toCachedEntryEntity() })

            prefRepository.setLastOfflineSupportUpdatedAt(System.currentTimeMillis())

            emit(Resource.Success(Unit))

        } catch (e: HttpException) {
            emit(
                // TODO: extract to string resources
                Resource.Error(message = "Oops, something went wrong!")
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    // TODO: extract to string resources
                    message = "Couldn't reach server. Please check your internet connection."
                )
            )
        }
    }

    override suspend fun isOfflineFullySupported(): Boolean {
        val lastUpdated = prefRepository
            .getLastOfflineSupportUpdatedAt()
            .firstOrNull()

        return lastUpdated != null
    }
}