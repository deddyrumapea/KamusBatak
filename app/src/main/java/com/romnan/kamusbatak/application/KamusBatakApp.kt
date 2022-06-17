package com.romnan.kamusbatak.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import logcat.AndroidLogcatLogger
import logcat.LogPriority

@HiltAndroidApp
class KamusBatakApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // Log all priorities in debug builds, no-op in release builds
        AndroidLogcatLogger.installOnDebuggableApp(
            application = this,
            minPriority = LogPriority.VERBOSE,
        )
    }
}