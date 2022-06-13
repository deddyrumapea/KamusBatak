package com.romnan.kamusbatak.di

import com.romnan.kamusbatak.core.data.local.CoreDatabase
import com.romnan.kamusbatak.featEntryDetail.data.repository.EntryDetailRepositoryImpl
import com.romnan.kamusbatak.featEntryDetail.domain.repository.EntryDetailRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EntryDetailModule {
    @Provides
    @Singleton
    fun provideRepository(
        coreDb: CoreDatabase,
    ): EntryDetailRepository {
        return EntryDetailRepositoryImpl(
            coreDao = coreDb.dao,
        )
    }
}