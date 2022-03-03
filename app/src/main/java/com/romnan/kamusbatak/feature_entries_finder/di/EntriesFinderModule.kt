package com.romnan.kamusbatak.feature_entries_finder.di

import com.romnan.kamusbatak.core.data.local.CoreDatabase
import com.romnan.kamusbatak.feature_entries_finder.data.remote.EntriesFinderApi
import com.romnan.kamusbatak.feature_entries_finder.data.repository.EntriesFinderRepositoryImpl
import com.romnan.kamusbatak.feature_entries_finder.domain.repository.EntriesFinderRepository
import com.romnan.kamusbatak.feature_entries_finder.domain.use_case.EntriesFinderUseCase
import com.romnan.kamusbatak.feature_entries_finder.domain.use_case.GetEntries
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EntriesFinderModule {
    @Provides
    @Singleton
    fun provideUseCase(
        repository: EntriesFinderRepository
    ): EntriesFinderUseCase {
        return EntriesFinderUseCase(GetEntries(repository))
    }

    @Provides
    @Singleton
    fun provideRepository(
        coreDb: CoreDatabase,
        api: EntriesFinderApi
    ): EntriesFinderRepository {
        return EntriesFinderRepositoryImpl(api, coreDb.dao)
    }

    @Provides
    @Singleton
    fun provideApi(coreRetrofit: Retrofit): EntriesFinderApi {
        return coreRetrofit.create(EntriesFinderApi::class.java)
    }
}