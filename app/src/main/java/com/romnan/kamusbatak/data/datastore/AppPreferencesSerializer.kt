package com.romnan.kamusbatak.data.datastore

import androidx.datastore.core.Serializer
import com.romnan.kamusbatak.domain.model.AppPreferences
import kotlinx.serialization.json.Json
import logcat.asLog
import logcat.logcat
import java.io.InputStream
import java.io.OutputStream

object AppPreferencesSerializer : Serializer<AppPreferences> {
    override val defaultValue: AppPreferences
        get() = AppPreferences()

    override suspend fun readFrom(
        input: InputStream
    ): AppPreferences {
        return try {
            Json.decodeFromString(
                deserializer = AppPreferences.serializer(),
                string = input.readBytes().decodeToString(),
            )
        } catch (e: Exception) {
            logcat { e.asLog() }
            defaultValue
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun writeTo(
        t: AppPreferences,
        output: OutputStream
    ) {
        output.write(
            Json.encodeToString(
                serializer = AppPreferences.serializer(),
                value = t,
            ).encodeToByteArray()
        )
    }
}