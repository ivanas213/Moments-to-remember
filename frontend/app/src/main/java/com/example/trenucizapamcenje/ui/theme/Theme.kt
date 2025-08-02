package com.example.trenucizapamcenje.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.material3.*

private val LightColorScheme = lightColorScheme(
    primary = PinkButton,
    onPrimary = White,
    secondary = PinkEventDescription,
    onSecondary = White,
    background = PinkBackground,
    onBackground = DarkPinkText,
    surface = White,
    onSurface = DarkPinkText,
    error = PinkError,
    onError = White
)

@Composable
fun MojaTema(
    useDarkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography(),
        shapes = Shapes(),
        content = content
    )
}
