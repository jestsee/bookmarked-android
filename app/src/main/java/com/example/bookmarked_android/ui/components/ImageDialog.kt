package com.example.bookmarked_android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.bookmarked_android.ui.theme.ASYNC_IMAGE_PLACEHOLDER

@Composable
fun ImageDialog(
    url: String,
    onDismissRequest: () -> Unit,
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var imageSize by remember { mutableStateOf(IntSize.Zero) }

    val minScale = 1f
    val maxScale = 3f

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .onSizeChanged { imageSize = it }
                .graphicsLayer(
                    scaleX = maxOf(minScale, minOf(maxScale, scale)),
                    scaleY = maxOf(minScale, minOf(maxScale, scale)),
                    translationX = offsetX,
                    translationY = offsetY
                )
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale = (scale * zoom).coerceIn(minScale, maxScale)

                        if (scale > 1) {
                            val maxX = (imageSize.width * (scale - 1)) / 2
                            val maxY = (imageSize.height * (scale - 1)) / 2

                            offsetX = (offsetX + pan.x * scale).coerceIn(-maxX, maxX)
//                            offsetY = (offsetY + pan.y * scale).coerceIn(-maxY, maxY)
                            return@detectTransformGestures
                        }
                        offsetX = 0f
                        offsetY = 0f
                    }
                },
            model = url,
            contentDescription = "Content image",
            contentScale = ContentScale.FillWidth,
            placeholder = ASYNC_IMAGE_PLACEHOLDER
        )


    }

}