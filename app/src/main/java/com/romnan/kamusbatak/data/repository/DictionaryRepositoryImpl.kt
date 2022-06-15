package com.romnan.kamusbatak.data.repository

import com.romnan.kamusbatak.R
import com.romnan.kamusbatak.data.datastore.AppPreferencesManager
import com.romnan.kamusbatak.data.retrofit.EntryApi
import com.romnan.kamusbatak.data.retrofit.dto.EntryDto
import com.romnan.kamusbatak.data.room.EntryDao
import com.romnan.kamusbatak.domain.model.Entry
import com.romnan.kamusbatak.domain.model.Language
import com.romnan.kamusbatak.domain.repository.DictionaryRepository
import com.romnan.kamusbatak.domain.util.Resource
import com.romnan.kamusbatak.domain.util.SimpleResource
import com.romnan.kamusbatak.domain.util.UIText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import logcat.logcat
import retrofit2.HttpException
import java.io.IOException

class DictionaryRepositoryImpl(
    private val entryApi: EntryApi,
    private val entryDao: EntryDao,
    private val appPreferencesManager: AppPreferencesManager
) : DictionaryRepository {

    override val localDbLastUpdatedAt: Flow<Long?>
        get() = appPreferencesManager.dataStore.data.map { it.localDbLastUpdatedAt }

    override fun getEntries(
        keyword: String,
        srcLang: Language,
    ): Flow<Resource<List<Entry>>> = flow {
        if (keyword.isBlank() || !keyword.all { it.isLetterOrDigit() }) {
            emit(Resource.Success(emptyList()))
            return@flow
        }

        val localEntries = entryDao
            .findByKeyword(keyword = keyword, srcLangCodeName = srcLang.codeName)
            .map { it.toEntry() }

        if (isFullOfflineSupported()) {
            emit(Resource.Success(data = localEntries))
            return@flow
        }

        try {
            emit(Resource.Loading(data = localEntries))

            val remoteEntries = entryApi.getEntries(
                params = mapOf(
                    EntryDto.Field.WORD to "like.%$keyword%".lowercase(),
                    EntryDto.Field.SRC_LANG to "eq.${srcLang.codeName}"
                )
            )

            entryDao.insert(remoteEntries.map { it.toEntryEntity() })
            val cachedEntries = entryDao
                .findByKeyword(keyword = keyword, srcLangCodeName = srcLang.codeName)
                .map { it.toEntry() }

            emit(Resource.Success(cachedEntries))
        } catch (e: Exception) {
            when (e) {
                is HttpException -> UIText.StringResource(R.string.em_http_exception)
                is IOException -> UIText.StringResource(R.string.em_io_exception)
                else -> UIText.StringResource(R.string.em_unknown)
            }.let { emit(Resource.Error(uiText = it, data = localEntries)) }
        }
    }

    override fun getEntry(
        id: Int,
    ): Flow<Resource<Entry>> = flow {
        emit(Resource.Loading())

        val entryEntity = entryDao.findById(id = id)

        emit(
            if (entryEntity != null) Resource.Success(entryEntity.toEntry())
            else Resource.Error(UIText.StringResource(R.string.em_unknown))
        )
    }

    override fun getBookmarkedEntries(
        srcLang: Language,
    ): Flow<Resource<List<Entry>>> = flow {
        emit(Resource.Loading())
        val entries = entryDao
            .findBookmarked(srcLangCodeName = srcLang.codeName)
            .map { it.toEntry() }
        emit(Resource.Success(entries))
    }

    override fun toggleBookmarkEntry(
        id: Int,
    ): Flow<Resource<Entry>> = flow {
        emit(Resource.Loading())

        val oldEntryEntity = entryDao.findById(id = id)
        entryDao.toggleBookmark(id = id)
        val newEntryEntity = entryDao.findById(id = id)

        when {
            newEntryEntity == null -> emit(Resource.Error(UIText.StringResource(R.string.em_unknown)))
            newEntryEntity.isBookmarked == oldEntryEntity?.isBookmarked -> {
                emit(Resource.Error(UIText.StringResource(R.string.em_unknown)))
            }
            else -> emit(Resource.Success(newEntryEntity.toEntry()))
        }
    }

    override fun updateLocalDb(): Flow<SimpleResource> = flow {
        emit(Resource.Loading())

        try {
            val latestEntryUpdatedAt = entryDao.getLatestEntryUpdatedAt()
            val params = when {
                isFullOfflineSupported() && !latestEntryUpdatedAt.isNullOrBlank() ->
                    mapOf(EntryDto.Field.UPDATED_AT to "gt.$latestEntryUpdatedAt")
                else -> emptyMap()
            }
            val remoteEntries = entryApi.getEntries(params = params)
            entryDao.insert(remoteEntries.map { it.toEntryEntity() })
            setLocalDbLastUpdatedAt(System.currentTimeMillis())
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

    private suspend fun setLocalDbLastUpdatedAt(currentTimeMillis: Long) {
        appPreferencesManager.dataStore.updateData {
            it.copy(localDbLastUpdatedAt = currentTimeMillis)
        }
    }

    private suspend fun isFullOfflineSupported(): Boolean {
        return localDbLastUpdatedAt.firstOrNull() != null
    }
}