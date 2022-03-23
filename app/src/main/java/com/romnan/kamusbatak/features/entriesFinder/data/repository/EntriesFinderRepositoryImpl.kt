package com.romnan.kamusbatak.features.entriesFinder.data.repository

import com.romnan.kamusbatak.R
import com.romnan.kamusbatak.core.data.local.CoreDao
import com.romnan.kamusbatak.core.data.remote.CoreApi
import com.romnan.kamusbatak.core.data.remote.dto.RemoteEntryDto
import com.romnan.kamusbatak.core.domain.model.Entry
import com.romnan.kamusbatak.core.domain.repository.OfflineSupportRepository
import com.romnan.kamusbatak.core.util.Language
import com.romnan.kamusbatak.core.util.Resource
import com.romnan.kamusbatak.core.util.UIText
import com.romnan.kamusbatak.features.entriesFinder.domain.repository.EntriesFinderRepository
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
        emit(Resource.Loading(data = emptyList()))

        val formattedKeyword = when (keyword.length) {
            in 0..3 -> "$keyword%"
            else -> "%$keyword%"
        }.lowercase()

        val localEntries = getLocalEntries(formattedKeyword, srcLang)
        emit(Resource.Loading(data = localEntries))

        try {
            val remoteEntries = getRemoteEntries(formattedKeyword, srcLang)
            coreDao.insertCachedEntries(remoteEntries.map { it.toCachedEntryEntity() })
        } catch (e: HttpException) {
            emit(
                if (offlineSupportRepository.isOfflineFullySupported()) {
                    Resource.Success(localEntries)
                } else {
                    Resource.Error(
                        uiText = UIText.StringResource(R.string.em_http_exception)
                    )
                }
            )
        } catch (e: IOException) {
            emit(
                if (offlineSupportRepository.isOfflineFullySupported()) {
                    Resource.Success(localEntries)
                } else {
                    Resource.Error(
                        uiText = UIText.StringResource(R.string.em_io_exception),
                        data = localEntries
                    )
                }
            )
        }

        val newEntries = getLocalEntries(formattedKeyword, srcLang)
        emit(Resource.Success(newEntries))
    }

    private suspend fun getLocalEntries(
        keyword: String,
        srcLang: Language
    ): List<Entry> {
        return coreDao
            .getCachedEntries(
                keyword = keyword,
                srcLang = srcLang.codename
            )
            .map { it.toEntry() }
    }

    private suspend fun getRemoteEntries(
        keyword: String,
        srcLang: Language
    ): List<RemoteEntryDto> {
        val params = mapOf(
            RemoteEntryDto.Field.WORD to "like.$keyword",
            RemoteEntryDto.Field.SRC_LANG to "eq.${srcLang.codename}"
        )
        return coreApi.getEntries(params = params)
    }
}