package com.lmalecic.lovefinderzz.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Immutable
data class ExtendedTypography(
    val bannerSmall: TextStyle = TextStyle.Default,
    val bannerMedium: TextStyle = TextStyle.Default,
    val bannerLarge: TextStyle = TextStyle.Default
)

val LocalExtendedTypography = staticCompositionLocalOf {
    ExtendedTypography()
}

val MaterialTheme.extendedTypography: ExtendedTypography
    @Composable
    @ReadOnlyComposable
    get() = LocalExtendedTypography.current

val MyExtendedTypography = ExtendedTypography(
    bannerSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),

    bannerMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),

    bannerLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    )
)