package com.example.bookmarked_android.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CustomShapeBox(
    modifier: Modifier = Modifier,
    filledColor: Color = Color.Red,
    cornerRadius: Dp,
    innerRadius: Dp = cornerRadius
) {
    var heightInDp by remember { mutableStateOf(0.dp) }
    var widthInDp by remember { mutableStateOf(0.dp) }
    val localDensity = LocalDensity.current

    Canvas(modifier = modifier.onGloballyPositioned { coordinates ->
        heightInDp = with(localDensity) {
            coordinates.size.height.toDp()
        }
        widthInDp = with(localDensity) {
            coordinates.size.width.toDp()
        }
    }) {

        val widthInPx = widthInDp.toPx()
        val heightInPx = heightInDp.toPx()
        val radius = cornerRadius.toPx()

        val outerRectanglePath = Path().apply {
            addRect(Rect(0f, 0f, widthInPx, heightInPx))
        }

        val innerRectanglePath = Path().apply {
            addRect(Rect(widthInPx - innerRadius.toPx(), 0f, widthInPx, innerRadius.toPx()))
        }

        val subtractedPath = Path().apply {
            op(outerRectanglePath, innerRectanglePath, PathOperation.Difference)
        }

        drawIntoCanvas { canvas ->
            canvas.drawOutline(
                outline = Outline.Generic(subtractedPath),
                paint = Paint().apply {
                    color = filledColor
                    pathEffect = PathEffect.cornerPathEffect(radius)
                }
            )
        }
    }
}

@Preview
@Composable
private fun CustomShapeBoxPreview() {
    CustomShapeBox(Modifier.size(400.dp), cornerRadius = 36.dp, innerRadius = 64.dp)
}
