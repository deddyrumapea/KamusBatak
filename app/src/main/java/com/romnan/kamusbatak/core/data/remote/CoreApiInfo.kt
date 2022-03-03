package com.romnan.kamusbatak.core.data.remote

object CoreApiInfo {
    init {
        System.loadLibrary("native-lib")
    }

    external fun baseUrl(): String
    external fun keyValue(): String
    external fun keyParam(): String
}