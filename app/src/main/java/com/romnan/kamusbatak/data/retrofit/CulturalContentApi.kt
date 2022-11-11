package com.romnan.kamusbatak.data.retrofit

import com.romnan.kamusbatak.domain.model.Partuturan
import com.romnan.kamusbatak.domain.model.Umpasa
import com.romnan.kamusbatak.domain.model.UmpasaCategory

interface CulturalContentApi {

    suspend fun getPartuturans(): List<Partuturan>

    suspend fun getUmpasas(
        category: UmpasaCategory,
    ): List<Umpasa>
}