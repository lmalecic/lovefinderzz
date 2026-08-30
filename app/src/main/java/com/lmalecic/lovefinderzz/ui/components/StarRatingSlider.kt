package com.lmalecic.lovefinderzz.ui.components

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lmalecic.lovefinderzz.R

private fun ratingFromPosition(positionX: Float, width: Float): Float {
    if (width <= 0f) {
        return 0.5f
    }

    val normalizedPosition = (positionX / width).coerceIn(0f, 1f)
    val halfStep = (normalizedPosition * 10f).toInt() + 1

    return halfStep.coerceIn(1, 10) / 2f
}

@Composable
fun StarRatingSlider(
    value: Float,
    isRated: Boolean,
    onValueChange: (Float) -> Unit,
    onValueChangeFinished: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val visualRating = if (isRated) value else 0f

    val currentOnValueChange = rememberUpdatedState(onValueChange)
    val currentOnValueChangeFinished = rememberUpdatedState(onValueChangeFinished)

    Row(
        modifier = modifier.pointerInput(Unit) {
            var dragRating = value

            detectHorizontalDragGestures(
                onDragStart = { position ->
                    dragRating = ratingFromPosition(
                        positionX = position.x,
                        width = size.width.toFloat()
                    )

                    currentOnValueChange.value(dragRating)
                },

                onHorizontalDrag = { change, _ ->
                    dragRating = ratingFromPosition(
                        positionX = change.position.x,
                        width = size.width.toFloat()
                    )

                    currentOnValueChange.value(dragRating)
                    change.consume()
                },

                onDragEnd = {
                    currentOnValueChangeFinished.value(dragRating)
                },

                onDragCancel = {}
            )
        },
        horizontalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        repeat(5) { index ->
            RatingStar(
                fillFraction = (visualRating - index).coerceIn(0f, 1f),
                modifier = Modifier.size(48.dp)
                    .pointerInput(index) {
                        detectTapGestures { position ->
                            val fraction = if (position.x < size.width / 2f) 0.5f else 1f
                            val selectedRating = index + fraction

                            currentOnValueChange.value(selectedRating)
                            currentOnValueChangeFinished.value(selectedRating)
                        }
                    }
            )
        }
    }
}

@Composable
private fun RatingStar(
    fillFraction: Float,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_star_outline),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxSize()
        )

        Icon(
            painter = painterResource(R.drawable.ic_star),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxSize()
                .drawWithContent {
                    clipRect(
                        right = size.width * fillFraction
                    ) {
                        this@drawWithContent.drawContent()
                    }
                }
        )
    }
}