package com.romnan.kamusbatak.featEntriesFinder.data.repository

import com.romnan.kamusbatak.R
import com.romnan.kamusbatak.core.data.local.CoreDao
import com.romnan.kamusbatak.core.data.local.entity.CachedEntryEntity
import com.romnan.kamusbatak.core.data.remote.CoreApi
import com.romnan.kamusbatak.core.data.remote.dto.RemoteEntryDto
import com.romnan.kamusbatak.core.domain.model.Entry
import com.romnan.kamusbatak.core.domain.model.Language
import com.romnan.kamusbatak.core.domain.repository.OfflineSupportRepository
import com.romnan.kamusbatak.core.util.Resource
import com.romnan.kamusbatak.core.util.UIText
import com.romnan.kamusbatak.featEntriesFinder.domain.repository.EntriesFinderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class EntriesFinderRepositoryImpl(
    private val coreApi: CoreApi,
    private val coreDao: CoreDao,
    private val offlineSupportRepository: OfflineSupportRepository
) : EntriesFinderRepository {

    override fun getEntries(
        keyword: String,
        srcLang: Language
    ): Flow<Resource<List<Entry>>> = flow {
        if (keyword.isBlank() || !keyword.all { it.isLetterOrDigit() }) {
            emit(Resource.Success(emptyList()))
            return@flow
        }

        val localEntries = getLocalEntries(keyword = keyword, srcLang = srcLang)

        if (offlineSupportRepository.isOfflineFullySupported()) {
            emit(Resource.Success(data = localEntries))
            return@flow
        }

        try {
            emit(Resource.Loading(data = localEntries))

            val remoteEntries = getRemoteEntries(keyword = keyword, srcLang = srcLang)
            coreDao.insertCachedEntries(remoteEntries.map { it.toCachedEntryEntity() })
            val newEntries = getLocalEntries(keyword = keyword, srcLang = srcLang)

            emit(Resource.Success(newEntries))
        } catch (e: Exception) {
            when (e) {
                is HttpException -> UIText.StringResource(R.string.em_http_exception)
                is IOException -> UIText.StringResource(R.string.em_io_exception)
                else -> UIText.StringResource(R.string.em_unknown)
            }.let { emit(Resource.Error(uiText = it, data = localEntries)) }
        }
    }

    private suspend fun getLocalEntries(
        keyword: String,
        srcLang: Language
    ): List<Entry> {
        return mutableListOf<CachedEntryEntity>()
            .apply {
                coreDao.getCachedEntries(
                    keyword = "$keyword%".lowercase(),
                    srcLangCodeName = srcLang.codeName
                ).let { addAll(it) }

                if (keyword.length > 1) coreDao.getCachedEntries(
                    keyword = "%$keyword%".lowercase(),
                    srcLangCodeName = srcLang.codeName
                ).let { addAll(it) }
            }
            .distinct()
            .map { it.toEntry() }
    }

    private suspend fun getRemoteEntries(
        keyword: String,
        srcLang: Language
    ): List<RemoteEntryDto> {
        val params = mapOf(
            RemoteEntryDto.Field.WORD to "like.%$keyword%",
            RemoteEntryDto.Field.SRC_LANG to "eq.${srcLang.codeName}"
        )
        return coreApi.getEntries(params = params)
    }
}