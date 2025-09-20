package com.romnan.kamusbatak.application

import android.app.Application
import android.content.pm.ApplicationInfo
import dagger.hilt.android.HiltAndroidApp
import logcat.AndroidLogcatLogger
import logcat.LogPriority

@HiltAndroidApp
class KamusBatakApp : Application() {

    val isDebuggable: Boolean
        get() = (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0

    override fun onCreate() {
        super.onCreate()

        // Log all priorities in debug builds, no-op in release builds
        AndroidLogcatLogger.installOnDebuggableApp(
            application = this,
            minPriority = LogPriority.VERBOSE,
        )
    }
}