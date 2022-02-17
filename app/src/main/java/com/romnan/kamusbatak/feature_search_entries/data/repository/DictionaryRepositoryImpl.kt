package com.romnan.kamusbatak.feature_search_entries.data.repository

import com.romnan.kamusbatak.core.util.Language
import com.romnan.kamusbatak.core.util.Resource
import com.romnan.kamusbatak.feature_search_entries.data.local.DictionaryDao
import com.romnan.kamusbatak.feature_search_entries.data.remote.DictionaryApi
import com.romnan.kamusbatak.feature_search_entries.data.remote.dto.EntryDto
import com.romnan.kamusbatak.feature_search_entries.domain.model.Entry
import com.romnan.kamusbatak.feature_search_entries.domain.repository.DictionaryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class DictionaryRepositoryImpl(
    private val api: DictionaryApi,
    private val dao: DictionaryDao
) : DictionaryRepository {
    override fun searchEntries(keyword: String, srcLang: Language): Flow<Resource<List<Entry>>> =
        flow {
            emit(Resource.Loading(data = emptyList()))

            val localEntries = getLocalEntries(keyword, srcLang)
            emit(Resource.Loading(data = localEntries))

            try {
                val remoteEntries = getRemoteEntries(keyword, srcLang)
                dao.insertEntries(remoteEntries.map { it.toEntryEntity() })
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
        return dao
            .getEntries(keyword = keyword, srcLang = srcLang.codename)
            .map { it.toEntry() }
    }

    private suspend fun getRemoteEntries(keyword: String, srcLang: Language): List<EntryDto> {
        val params = mapOf(
            "word" to "like.$keyword",
            "src_lang" to "eq.${srcLang.codename}"
        )
        return api.getEntries(params = params)
    }
}