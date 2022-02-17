package com.romnan.kamusbatak.core.util

object ApiInfo {
    init {
        System.loadLibrary("native-lib")
    }

    external fun baseUrl(): String
    external fun key(): String
}