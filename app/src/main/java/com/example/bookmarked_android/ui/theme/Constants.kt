package com.example.bookmarked_android.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.unit.dp

val PADDING = 16.dp

val ASYNC_IMAGE_PLACEHOLDER = BrushPainter(
    Brush.linearGradient(
        listOf(
            Color(color = 0xFFFFFFFF),
            Color(color = 0xFFDDDDDD),
        )
    )
)