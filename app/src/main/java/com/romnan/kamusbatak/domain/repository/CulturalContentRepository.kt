package com.romnan.kamusbatak.domain.repository

import com.romnan.kamusbatak.domain.model.Partuturan
import com.romnan.kamusbatak.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface CulturalContentRepository {

    fun getPartuturans(): Flow<Resource<List<Partuturan>>>

}