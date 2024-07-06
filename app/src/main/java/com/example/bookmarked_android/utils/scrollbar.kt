package com.example.bookmarked_android.utils

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.verticalScrollBar(
    state: ScrollState,
    color: Color = Color.Gray,
    ratio: Float = 3f,
    width: Dp = 8.dp
): Modifier {
    val targetAlpha = if (state.isScrollInProgress) 1f else .5f
    val duration = if (state.isScrollInProgress) 150 else 500

    val horizontalOffset = 4.dp
    val verticalOffset = 6.dp

    val alpha by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = tween(durationMillis = duration), label = ""
    )

    return drawWithContent {
        drawContent()

        val barHeight = (this.size.height / ratio)
        val barRange = (this.size.height - barHeight) / state.maxValue
        val position = state.value * barRange

        drawRoundRect(
            color = color.copy(alpha),
            topLeft = Offset(
                this.size.width - width.toPx() - horizontalOffset.toPx(),
                position + verticalOffset.toPx()
            ),
            cornerRadius = CornerRadius(x = 25f, y = 25f),
            size = Size(width.toPx(), barHeight - 1.5f * verticalOffset.toPx()),
        )
    }
}