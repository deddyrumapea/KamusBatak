package com.romnan.kamusbatak.feature_search_entries.data.repository

import com.romnan.kamusbatak.core.util.Resource
import com.romnan.kamusbatak.feature_search_entries.data.local.DictionaryDao
import com.romnan.kamusbatak.feature_search_entries.data.remote.DictionaryApi
import com.romnan.kamusbatak.feature_search_entries.data.remote.dto.EntryDto
import com.romnan.kamusbatak.core.util.Language
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
    override fun searchEntries(keyword: String, kwLanguage: Language): Flow<Resource<List<Entry>>> =
        flow {
            emit(Resource.Loading(data = emptyList<Entry>()))

            val localEntries = getLocalEntries(keyword, kwLanguage)
            emit(Resource.Loading(data = localEntries))

            try {
                val remoteEntries = getRemoteEntries(keyword, kwLanguage)
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

            val newEntries = getLocalEntries(keyword, kwLanguage)
            emit(Resource.Success(newEntries))
        }

    private suspend fun getLocalEntries(keyword: String, kwLanguage: Language): List<Entry> {
        return when (kwLanguage) {
            is Language.Ind -> dao.getEntriesWithIndKeyword(keyword).map { it.toEntry() }
            is Language.Btk -> dao.getEntriesWithBtkKeyword(keyword).map { it.toEntry() }
        }
    }

    suspend fun getRemoteEntries(keyword: String, kwLanguage: Language): List<EntryDto> {
        val params = mapOf(
            when (kwLanguage) {
                is Language.Ind -> "ind_word"
                is Language.Btk -> "btk_word"
            } to "like.$keyword%"
        )
        return api.getEntries(params = params)
    }
}