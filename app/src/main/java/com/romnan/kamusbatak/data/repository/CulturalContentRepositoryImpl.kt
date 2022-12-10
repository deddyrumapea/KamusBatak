package com.romnan.kamusbatak.data.repository

import com.romnan.kamusbatak.data.retrofit.CulturalContentApi
import com.romnan.kamusbatak.domain.model.Partuturan
import com.romnan.kamusbatak.domain.repository.CulturalContentRepository
import com.romnan.kamusbatak.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CulturalContentRepositoryImpl(
    private val culturalContentApi: CulturalContentApi
) : CulturalContentRepository {

    override fun getPartuturans(): Flow<Resource<List<Partuturan>>> = flow {
        emit(Resource.Loading())
        val result = culturalContentApi.getPartuturans()
        emit(Resource.Success(result))
    }
}