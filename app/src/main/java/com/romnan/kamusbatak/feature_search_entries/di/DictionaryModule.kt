package com.romnan.kamusbatak.feature_search_entries.di

import android.app.Application
import androidx.room.Room
import com.romnan.kamusbatak.core.util.ApiInfo
import com.romnan.kamusbatak.feature_search_entries.data.local.DictionaryDatabase
import com.romnan.kamusbatak.feature_search_entries.data.remote.DictionaryApi
import com.romnan.kamusbatak.feature_search_entries.data.repository.DictionaryRepositoryImpl
import com.romnan.kamusbatak.feature_search_entries.domain.repository.DictionaryRepository
import com.romnan.kamusbatak.feature_search_entries.domain.use_case.SearchEntries
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DictionaryModule {
    @Provides
    @Singleton
    fun provideSearchWithBatakKeyword(
        repository: DictionaryRepository
    ): SearchEntries {
        return SearchEntries(repository)
    }

    @Provides
    @Singleton
    fun provideDictionaryRepository(
        db: DictionaryDatabase,
        api: DictionaryApi
    ): DictionaryRepository {
        return DictionaryRepositoryImpl(api, db.dao)
    }

    @Provides
    @Singleton
    fun provideDictionaryDatabse(app: Application): DictionaryDatabase {
        return Room.databaseBuilder(
            app,
            DictionaryDatabase::class.java,
            "dictionary_batak" // TODO: move db name to database class
        ).build()
    }

    @Provides
    @Singleton
    fun provideDictionaryApi(): DictionaryApi {
        val interceptor = Interceptor { chain ->
            val request = chain
                .request()
                .newBuilder()
                .addHeader(
                    ApiInfo.keyParam(),
                    ApiInfo.keyValue()
                )
                .build()
            chain.proceed(request)
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(ApiInfo.baseUrl())
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DictionaryApi::class.java)
    }
}