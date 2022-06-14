package com.romnan.kamusbatak.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.romnan.kamusbatak.application.SecretValues
import com.romnan.kamusbatak.data.local.CoreDatabase
import com.romnan.kamusbatak.data.preferences.CorePreferences
import com.romnan.kamusbatak.data.remote.CoreApi
import com.romnan.kamusbatak.data.repository.EntriesFinderRepositoryImpl
import com.romnan.kamusbatak.data.repository.EntryDetailRepositoryImpl
import com.romnan.kamusbatak.data.repository.OfflineSupportRepositoryImpl
import com.romnan.kamusbatak.domain.repository.EntriesFinderRepository
import com.romnan.kamusbatak.domain.repository.EntryDetailRepository
import com.romnan.kamusbatak.domain.repository.OfflineSupportRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
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
    fun provideCoreApi(coreRetrofit: Retrofit): CoreApi {
        return coreRetrofit.create(CoreApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCoreDatabase(app: Application): CoreDatabase {
        val passphrase: ByteArray = SQLiteDatabase.getBytes(
            SecretValues.dbPassphrase().toCharArray()
        )
        val supportFactory = SupportFactory(passphrase)

        return Room
            .databaseBuilder(
                app,
                CoreDatabase::class.java,
                CoreDatabase.NAME
            )
            .openHelperFactory(supportFactory)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideCoreRetrofit(): Retrofit {
        val interceptor = Interceptor { chain ->
            val request = chain
                .request()
                .newBuilder()
                .addHeader(
                    SecretValues.keyParam(),
                    SecretValues.keyValue()
                )
                .build()
            chain.proceed(request)
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(SecretValues.baseUrl())
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideCorePreferences(
        @ApplicationContext appContext: Context
    ): CorePreferences {
        return CorePreferences(appContext)
    }

    @Provides
    @Singleton
    fun provideOfflineSupportRepository(
        coreApi: CoreApi,
        coreDb: CoreDatabase,
        corePref: CorePreferences
    ): OfflineSupportRepository {
        return OfflineSupportRepositoryImpl(
            coreApi = coreApi,
            coreDao = coreDb.dao,
            corePref = corePref
        )
    }

    @Provides
    @Singleton
    fun provideEntriesFinderRepository(
        coreDb: CoreDatabase,
        api: CoreApi,
        offlineSupportRepository: OfflineSupportRepository
    ): EntriesFinderRepository {
        return EntriesFinderRepositoryImpl(api, coreDb.dao, offlineSupportRepository)
    }

    @Provides
    @Singleton
    fun provideEntryDetailRepository(
        coreDb: CoreDatabase,
    ): EntryDetailRepository {
        return EntryDetailRepositoryImpl(
            coreDao = coreDb.dao,
        )
    }
}