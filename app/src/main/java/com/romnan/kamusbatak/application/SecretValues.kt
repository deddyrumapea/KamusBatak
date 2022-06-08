package com.romnan.kamusbatak.application

object SecretValues {
    init {
        System.loadLibrary("native-lib")
    }

    external fun baseUrl(): String
    external fun keyValue(): String
    external fun keyParam(): String
    external fun dbPassphrase(): String
}