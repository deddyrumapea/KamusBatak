package com.romnan.kamusbatak.core.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import com.ramcosta.composedestinations.DestinationsNavHost
import com.romnan.kamusbatak.features.NavGraphs
import com.romnan.kamusbatak.ui.theme.KamusBatakTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KamusBatakTheme {
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}