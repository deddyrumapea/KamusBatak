package com.romnan.kamusbatak.feature_entries_finder.data.repository

import com.romnan.kamusbatak.core.util.Language
import com.romnan.kamusbatak.core.util.Resource
import com.romnan.kamusbatak.core.data.local.CoreDao
import com.romnan.kamusbatak.feature_entries_finder.data.remote.EntriesFinderApi
import com.romnan.kamusbatak.feature_entries_finder.data.remote.dto.FoundEntryDto
import com.romnan.kamusbatak.core.domain.model.Entry
import com.romnan.kamusbatak.feature_entries_finder.domain.repository.EntriesFinderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class EntriesFinderRepositoryImpl(
    private val api: EntriesFinderApi,
    private val coreDao: CoreDao
) : EntriesFinderRepository {
    override fun getEntries(keyword: String, srcLang: Language): Flow<Resource<List<Entry>>> =
        flow {
            emit(Resource.Loading(data = emptyList()))

            val localEntries = getLocalEntries(keyword, srcLang)
            emit(Resource.Loading(data = localEntries))

            try {
                val remoteEntries = getRemoteEntries(keyword, srcLang)
                coreDao.insertCachedEntries(remoteEntries.map { it.toCachedEntryEntity() })
            } catch (e: HttpException) {
                emit(
                    // TODO: extract to string resources
                    Resource.Error(
                        message = "Oops, something went wrong!",
                        data = localEntries
                    )
                )
            } catch (e: IOException) {
                emit(
                    Resource.Error(
                        // TODO: extract to string resources
                        message = "Couldn't reach server. Please check your internet connection.",
                        data = localEntries
                    )
                )
            }

            val newEntries = getLocalEntries(keyword, srcLang)
            emit(Resource.Success(newEntries))
        }

    private suspend fun getLocalEntries(keyword: String, srcLang: Language): List<Entry> {
        return coreDao
            .getCachedEntries(keyword = keyword, srcLang = srcLang.codename)
            .map { it.toEntry() }
    }

    private suspend fun getRemoteEntries(keyword: String, srcLang: Language): List<FoundEntryDto> {
        val params = mapOf(
            "word" to "like.$keyword",
            "src_lang" to "eq.${srcLang.codename}"
        )
        return api.getEntries(params = params)
    }
}