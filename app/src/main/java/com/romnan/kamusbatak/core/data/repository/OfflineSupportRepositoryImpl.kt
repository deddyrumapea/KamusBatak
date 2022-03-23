package com.romnan.kamusbatak.core.data.repository

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import com.romnan.kamusbatak.R
import com.romnan.kamusbatak.core.data.local.CoreDao
import com.romnan.kamusbatak.core.data.preferences.CorePreferences
import com.romnan.kamusbatak.core.data.remote.CoreApi
import com.romnan.kamusbatak.core.data.remote.dto.RemoteEntryDto
import com.romnan.kamusbatak.core.domain.repository.OfflineSupportRepository
import com.romnan.kamusbatak.core.util.Resource
import com.romnan.kamusbatak.core.util.SimpleResource
import com.romnan.kamusbatak.core.util.UIText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import okio.IOException
import retrofit2.HttpException

class OfflineSupportRepositoryImpl(
    private val coreApi: CoreApi,
    private val coreDao: CoreDao,
    private val corePref: CorePreferences
) : OfflineSupportRepository {

    private val keyLastUpdatedAt = longPreferencesKey(KEY_NAME_LAST_UPDATED_AT)

    override fun downloadUpdate(): Flow<SimpleResource> = flow {
        emit(Resource.Loading())

        try {
            val latestEntryUpdatedAt = coreDao.getLatestEntryUpdatedAt()

            val params = when {
                latestEntryUpdatedAt.isNullOrEmpty() -> emptyMap()
                else -> mapOf(RemoteEntryDto.Field.UPDATED_AT to "gt.$latestEntryUpdatedAt")
            }

            val remoteEntries = coreApi.getEntries(params = params)

            coreDao.insertCachedEntries(remoteEntries.map { it.toCachedEntryEntity() })

            setLastUpdatedAt(System.currentTimeMillis())

            emit(Resource.Success(Unit))

        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    uiText = UIText.StringResource(R.string.em_http_exception)
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    uiText = UIText.StringResource(R.string.em_io_exception)
                )
            )
        }
    }

    override suspend fun isOfflineFullySupported(): Boolean {
        val lastUpdated = getLastUpdatedAt().firstOrNull()
        return lastUpdated != null
    }

    override fun getLastUpdatedAt(): Flow<Long?> {
        return corePref.dataStore.data.map { pref ->
            pref[keyLastUpdatedAt]
        }
    }

    private suspend fun setLastUpdatedAt(timeInMillis: Long) {
        corePref.dataStore.edit { pref ->
            pref[keyLastUpdatedAt] = timeInMillis
        }
    }

    companion object {
        private const val KEY_NAME_LAST_UPDATED_AT = "key_name_last_updated_at"
    }
}