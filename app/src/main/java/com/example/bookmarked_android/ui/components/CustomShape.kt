package com.example.bookmarked_android.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CustomShapeBox(modifier: Modifier = Modifier, width: Dp, height: Dp = width, cornerRadius: Dp) {
    Canvas(modifier = modifier.size(width, height)) {
        val widthInPx = width.toPx()
        val heightInPx = height.toPx()
        val radius = cornerRadius.toPx()

        val outerRectanglePath = Path().apply {
            addRect(Rect(0f, 0f, widthInPx, heightInPx))
        }

        val innerRectanglePath = Path().apply {
            addRect(Rect(widthInPx - radius, 0f, widthInPx, radius))
        }

        val subtractedPath = Path().apply {
            op(outerRectanglePath, innerRectanglePath, PathOperation.Difference)
        }

        drawIntoCanvas {
            canvas -> canvas.drawOutline(
                outline = Outline.Generic(subtractedPath),
                paint = Paint().apply {
                    color = Color.Red
                    pathEffect = PathEffect.cornerPathEffect(radius)
                }
            )
        }
    }
}

@Preview
@Composable
fun CustomShapeBoxPreview() {
    CustomShapeBox(width = 200.dp, height = 400.dp,cornerRadius = 40.dp)
}
