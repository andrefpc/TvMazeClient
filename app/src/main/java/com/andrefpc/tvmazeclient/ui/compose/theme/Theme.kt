package com.andrefpc.tvmazeclient.ui.compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorScheme = darkColorScheme(
    primary = Purple200,
    secondary = Purple700,
    tertiary = Color.Black,
    onPrimary = Color.Black,
    onTertiary = Color.White,
    background = Color.Black,
    surfaceContainer = Color.DarkGray

)

private val LightColorScheme = lightColorScheme(
    primary = Purple500,
    secondary = Purple700,
    tertiary = Purple500,
    onPrimary = Color.White,
    background = Color.White,
    surfaceContainer = Color.White
)

@Composable
fun TVMazeClientTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = Purple700,
        darkIcons = false
    )
    systemUiController.setNavigationBarColor(
        color = Color.Black,
        darkIcons = false
    )

    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}