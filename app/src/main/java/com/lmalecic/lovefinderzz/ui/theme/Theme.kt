package com.lmalecic.lovefinderzz.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Pink200,
    secondary = PinkGrey40,
    tertiary = Pink40,

    background = LightPink30,
    surface = Color(0xFFFFFBFE),
    surfaceVariant = Pink30,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),

    primaryContainer = Color(0xFFEEC9DB),
    onPrimaryContainer = Color(0xFF4D2A3B),

    surfaceContainer = LightPink30,
)

@Composable
fun LovefinderzzTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val extendedColors = when {
        darkTheme -> LightExtendedColors
        else -> LightExtendedColors
    }

    CompositionLocalProvider(
        LocalExtendedColors provides extendedColors,
        LocalExtendedTypography provides MyExtendedTypography
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}