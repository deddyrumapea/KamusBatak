package com.romnan.kamusbatak.di

import com.romnan.kamusbatak.core.data.local.CoreDatabase
import com.romnan.kamusbatak.core.data.remote.CoreApi
import com.romnan.kamusbatak.core.domain.repository.OfflineSupportRepository
import com.romnan.kamusbatak.featEntriesFinder.data.repository.EntriesFinderRepositoryImpl
import com.romnan.kamusbatak.featEntriesFinder.domain.repository.EntriesFinderRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EntriesFinderModule {
    @Provides
    @Singleton
    fun provideRepository(
        coreDb: CoreDatabase,
        api: CoreApi,
        offlineSupportRepository: OfflineSupportRepository
    ): EntriesFinderRepository {
        return EntriesFinderRepositoryImpl(api, coreDb.dao, offlineSupportRepository)
    }
}