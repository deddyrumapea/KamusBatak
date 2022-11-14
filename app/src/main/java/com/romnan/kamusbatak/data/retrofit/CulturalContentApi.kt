package com.romnan.kamusbatak.data.retrofit

import com.romnan.kamusbatak.domain.model.Partuturan

interface CulturalContentApi {

    suspend fun getPartuturans(): List<Partuturan>

}