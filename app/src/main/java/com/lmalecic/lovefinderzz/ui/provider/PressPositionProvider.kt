package com.lmalecic.lovefinderzz.ui.provider

import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.PopupPositionProvider

class PressPositionProvider(
    private val pressPosition: IntOffset
) : PopupPositionProvider {

    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        val requestedX = anchorBounds.left + pressPosition.x
        val requestedY = anchorBounds.top + pressPosition.y
        val maximumX = (windowSize.width - popupContentSize.width).coerceAtLeast(0)
        val maximumY = (windowSize.height - popupContentSize.height).coerceAtLeast(0)

        return IntOffset(
            x = requestedX.coerceIn(0, maximumX),
            y = requestedY.coerceIn(0, maximumY)
        )
    }
}