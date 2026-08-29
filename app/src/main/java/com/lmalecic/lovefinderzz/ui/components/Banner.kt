package com.lmalecic.lovefinderzz.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.lmalecic.lovefinderzz.ui.theme.extendedTypography

enum class BannerSize {
    SMALL, MEDIUM, LARGE
}

@Composable
fun Banner(
    modifier: Modifier = Modifier,
    text: String = "",
    iconPainter: Painter? = null,
    containerColor: Color = Color.Unspecified,
    contentColor: Color = Color.Unspecified,
    bannerSize: BannerSize = BannerSize.SMALL,
    content: BannerScope.(Float) -> Unit = {},
) {
    val scale = when (bannerSize) {
        BannerSize.SMALL -> 1.0f
        BannerSize.MEDIUM -> MaterialTheme.extendedTypography.bannerMedium.lineHeight.value / MaterialTheme.extendedTypography.bannerSmall.lineHeight.value
        BannerSize.LARGE -> MaterialTheme.extendedTypography.bannerLarge.lineHeight.value / MaterialTheme.extendedTypography.bannerSmall.lineHeight.value
    }

    val scope = BannerScope().apply {
        content(scale)
    }

    val endPadding = 6.dp * scale
    val startPadding = when {
        scope.prependContent != null -> 0.dp
        iconPainter != null -> 2.dp * scale
        else -> endPadding
    }

    val textStyle = when (bannerSize) {
        BannerSize.SMALL -> MaterialTheme.extendedTypography.bannerSmall
        BannerSize.MEDIUM -> MaterialTheme.extendedTypography.bannerMedium
        BannerSize.LARGE -> MaterialTheme.extendedTypography.bannerLarge
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(1.5.dp * scale),
            modifier = Modifier.padding(startPadding, 0.5.dp * scale, endPadding, 0.5.dp * scale)
        ) {
            scope.prependContent?.invoke()

            if (iconPainter != null) {
                Icon(
                    painter = iconPainter,
                    contentDescription = null,
                    modifier = Modifier.size(12.dp * scale)
                )
            }

            if (text.isNotBlank()) {
                Text(
                    text = text,
                    style = textStyle,
                )
            }

            scope.appendContent?.invoke()
        }
    }
}

class BannerScope internal constructor() {
    internal var prependContent: (@Composable () -> Unit)? = null
    internal var appendContent: (@Composable () -> Unit)? = null

    fun prepend(content: @Composable () -> Unit) {
        prependContent = content
    }

    fun append(content: @Composable () -> Unit) {
        appendContent = content
    }
}
