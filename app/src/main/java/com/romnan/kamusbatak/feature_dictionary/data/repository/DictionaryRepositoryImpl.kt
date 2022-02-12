package com.romnan.kamusbatak.feature_dictionary.data.repository

import com.romnan.kamusbatak.core.util.Resource
import com.romnan.kamusbatak.feature_dictionary.data.local.DictionaryDao
import com.romnan.kamusbatak.feature_dictionary.data.remote.DictionaryApi
import com.romnan.kamusbatak.feature_dictionary.domain.model.Entry
import com.romnan.kamusbatak.feature_dictionary.domain.repository.DictionaryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class DictionaryRepositoryImpl(
    private val api: DictionaryApi,
    private val dao: DictionaryDao
) : DictionaryRepository {

    override fun searchWithBatakKeyword(keyword: String): Flow<Resource<List<Entry>>> = flow {
        emit(Resource.Loading())

        val entries = dao.searchWithBatakKeyword(keyword).map { it.toEntry() }
        emit(Resource.Loading(data = entries))


        try {
            // TODO: move like.%keyword% to Api service interface
            val remoteEntries = api.searchWithBatakKeyword(keyword = "like.%$keyword%")
            dao.deleteEntries(remoteEntries.map { it.btkWord })
            dao.insertEntries(remoteEntries.map { it.toEntryEntity() })
        } catch (e: HttpException) {
            emit(
                // TODO: extract to string resources
                Resource.Error(
                    message = "Oops, something went wrong!", // TODO: extract string resource
                    data = entries
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    // TODO: extract to string resources
                    message = "Couldn't reach server. Please check your internet connection.",
                    data = entries
                )
            )
        }

        val newEntries = dao.searchWithBatakKeyword(keyword).map { it.toEntry() }
        emit(Resource.Success(newEntries))
    }
}