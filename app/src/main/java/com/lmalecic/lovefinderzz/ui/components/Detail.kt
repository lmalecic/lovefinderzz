package com.lmalecic.lovefinderzz.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lmalecic.lovefinderzz.ui.theme.extendedColors

@Composable
fun Detail(
    labelText: String,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = labelText,
            style = MaterialTheme.typography.bodyMedium.merge(
                fontWeight = FontWeight.SemiBold
            )
        )

        DetailSpacer()

        content()
    }
}

@Composable
fun Detail(
    labelText: String,
    valueText: String,
    valueIconPainter: Painter? = null
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = labelText,
            style = MaterialTheme.typography.bodyMedium.merge(
                fontWeight = FontWeight.SemiBold
            )
        )

        DetailSpacer()

        Banner(
            text = valueText,
            containerColor = MaterialTheme.extendedColors.bannerOpaque,
            bannerSize = BannerSize.LARGE,
            iconPainter = valueIconPainter
        )
    }
}

@Composable
private fun RowScope.DetailSpacer() {
    Canvas(
        modifier = Modifier
            .weight(1f)
            .padding(horizontal = 8.dp)
            .height(1.dp)
    ) {
        drawLine(
            color = androidx.compose.ui.graphics.Color.LightGray,
            start = androidx.compose.ui.geometry.Offset(0f, size.height),
            end = androidx.compose.ui.geometry.Offset(size.width, size.height),
            pathEffect = PathEffect.dashPathEffect(
                intervals = floatArrayOf(6f, 6f),
                phase = 0f
            ),
            strokeWidth = Stroke.DefaultMiter
        )
    }
}