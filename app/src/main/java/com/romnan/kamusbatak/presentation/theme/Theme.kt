package com.romnan.kamusbatak.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val LightColorPalette = lightColors(
    primary = Red800,
    primaryVariant = Red600,
    onPrimary = Color.White,

    secondary = Blue600,
    secondaryVariant = Blue200,
    onSecondary = Color.Black,

    background = BlueGray50,
    onBackground = Color.Black,

    surface = Color.White,
    onSurface = Color.Black,
)

private val DarkColorPalette = darkColors(
    primary = Red700,
    primaryVariant = Red400,
    onPrimary = Color.White,

    secondary = Blue600,
    secondaryVariant = Blue200,
    onSecondary = Color.White,

    background = Color.Black,
    onBackground = Color.White,

    surface = AlmostBlack,
    onSurface = Color.White
)

@Composable
fun KamusBatakTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        if (darkTheme) Color.Black
        else LightColorPalette.primary
    )

    CompositionLocalProvider(LocalSpacing provides Spacing()) {
        MaterialTheme(
            colors = if (darkTheme) DarkColorPalette else LightColorPalette,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}