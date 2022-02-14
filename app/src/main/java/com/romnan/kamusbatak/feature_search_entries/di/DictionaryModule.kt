package com.romnan.kamusbatak.feature_search_entries.di

import android.app.Application
import androidx.room.Room
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
import okhttp3.logging.HttpLoggingInterceptor
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
        // TODO: hide api keys
        val headerInterceptor = Interceptor { chain ->
            val request = chain
                .request()
                .newBuilder()
                .addHeader(
                    "apikey",
                    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoiYW5vbiIsImlhdCI6MTY0MzgxMDcyNiwiZXhwIjoxOTU5Mzg2NzI2fQ.XBDJKVy3FKXoS-1boDHNN4JixbXxTFy_44nRRATpYUA"
                )
                .addHeader(
                    "Authorization",
                    "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoiYW5vbiIsImlhdCI6MTY0MzgxMDcyNiwiZXhwIjoxOTU5Mzg2NzI2fQ.XBDJKVy3FKXoS-1boDHNN4JixbXxTFy_44nRRATpYUA"
                )
                .build()
            chain.proceed(request)
        }

        val loggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(DictionaryApi.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DictionaryApi::class.java)
    }
}