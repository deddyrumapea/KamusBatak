package com.romnan.kamusbatak.featEntriesFinder.data.repository

import com.romnan.kamusbatak.R
import com.romnan.kamusbatak.core.data.local.CoreDao
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

        val localEntries = coreDao
            .getEntries(keyword = keyword, srcLangCodeName = srcLang.codeName)
            .map { it.toEntry() }

        if (offlineSupportRepository.isFullySupported()) {
            emit(Resource.Success(data = localEntries))
            return@flow
        }

        try {
            emit(Resource.Loading(data = localEntries))

            val remoteEntries = coreApi.getEntries(
                params = mapOf(
                    RemoteEntryDto.Field.WORD to "like.%$keyword%".lowercase(),
                    RemoteEntryDto.Field.SRC_LANG to "eq.${srcLang.codeName}"
                )
            )

            coreDao.insertEntries(remoteEntries.map { it.toEntryEntity() })
            val cachedEntries = coreDao
                .getEntries(keyword = keyword, srcLangCodeName = srcLang.codeName)
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
}