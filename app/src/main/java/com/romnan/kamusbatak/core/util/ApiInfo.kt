package com.romnan.kamusbatak.core.util

object ApiInfo {
    init {
        System.loadLibrary("native-lib")
    }

    external fun baseUrl(): String
    external fun keyValue(): String
    external fun keyParam(): String
}