package com.lmalecic.lovefinderzz.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.lmalecic.lovefinderzz.entity.CharacterStatus

@Immutable
data class ExtendedColors(
    val bannerGreen: Color = Color.Unspecified,
    val bannerRed: Color = Color.Unspecified,
    val bannerOpaque: Color = Color.Unspecified,
    val bannerGrey: Color = Color.Unspecified,
    val onBannerGreen: Color = Color.Unspecified,
    val onBannerRed: Color = Color.Unspecified,
    val onBannerOpaque: Color = Color.Unspecified,
    val onBannerGrey: Color = Color.Unspecified
)

internal val LightExtendedColors = ExtendedColors(
    bannerGreen = Green200,
    bannerRed = Red200,
    bannerOpaque = OpaqueBlack16,
    bannerGrey = Grey200,
    onBannerGreen = Color.White,
    onBannerRed = Color.White,
    onBannerGrey = Color.White
)

internal val LocalExtendedColors = staticCompositionLocalOf {
    ExtendedColors()
}

val MaterialTheme.extendedColors: ExtendedColors
    @Composable
    @ReadOnlyComposable
    get() = LocalExtendedColors.current

@Composable
@ReadOnlyComposable
fun CharacterStatus.getColor() : Color {
    return when (this) {
        CharacterStatus.ALIVE -> MaterialTheme.extendedColors.bannerGreen
        CharacterStatus.DEAD -> MaterialTheme.extendedColors.bannerRed
        else -> MaterialTheme.extendedColors.bannerGrey
    }
}

@Composable
@ReadOnlyComposable
fun CharacterStatus.getContentColor() : Color {
    return when (this) {
        CharacterStatus.ALIVE -> MaterialTheme.extendedColors.onBannerGreen
        CharacterStatus.DEAD -> MaterialTheme.extendedColors.onBannerRed
        else -> MaterialTheme.extendedColors.onBannerGrey
    }
}