package com.romnan.kamusbatak.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.romnan.kamusbatak.application.SecretValues
import com.romnan.kamusbatak.data.datastore.AppPreferencesManager
import com.romnan.kamusbatak.data.local.LocalCulturalContentApi
import com.romnan.kamusbatak.data.repository.CulturalContentRepositoryImpl
import com.romnan.kamusbatak.data.repository.DictionaryRepositoryImpl
import com.romnan.kamusbatak.data.repository.PreferencesRepositoryImpl
import com.romnan.kamusbatak.data.retrofit.CulturalContentApi
import com.romnan.kamusbatak.data.retrofit.EntryApi
import com.romnan.kamusbatak.data.room.AppDatabase
import com.romnan.kamusbatak.domain.repository.CulturalContentRepository
import com.romnan.kamusbatak.domain.repository.DictionaryRepository
import com.romnan.kamusbatak.domain.repository.PreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideAppDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app, AppDatabase::class.java, AppDatabase.NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideAppRetrofit(): Retrofit {
        val interceptor = Interceptor { chain ->
            val request = chain.request().newBuilder().addHeader(
                SecretValues.keyParam(), SecretValues.keyValue()
            ).build()
            chain.proceed(request)
        }

        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        return Retrofit.Builder().baseUrl(SecretValues.baseUrl()).client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    @Singleton
    fun provideAppPreferencesManager(
        @ApplicationContext appContext: Context
    ): AppPreferencesManager {
        return AppPreferencesManager(appContext)
    }

    @Provides
    @Singleton
    fun provideEntryApi(appRetrofit: Retrofit): EntryApi {
        return appRetrofit.create(EntryApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDictionaryRepository(
        entryApi: EntryApi, appDatabase: AppDatabase, appPreferencesManager: AppPreferencesManager
    ): DictionaryRepository {
        return DictionaryRepositoryImpl(
            entryApi = entryApi,
            entryDao = appDatabase.entryDao,
            appPreferencesManager = appPreferencesManager,
        )
    }

    @Provides
    @Singleton
    fun providePreferencesRepository(
        appPreferencesManager: AppPreferencesManager
    ): PreferencesRepository {
        return PreferencesRepositoryImpl(
            appPreferencesManager = appPreferencesManager,
        )
    }

    @Provides
    @Singleton
    fun provideCulturalContentRepository(
        culturalContentApi: CulturalContentApi
    ): CulturalContentRepository {
        return CulturalContentRepositoryImpl(
            culturalContentApi = culturalContentApi
        )
    }

    @Provides
    @Singleton
    fun provideCulturalContentApi(): CulturalContentApi {
        return LocalCulturalContentApi()
    }
}