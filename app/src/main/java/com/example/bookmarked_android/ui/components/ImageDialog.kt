package com.example.bookmarked_android.ui.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bookmarked_android.ui.theme.ASYNC_IMAGE_PLACEHOLDER

@OptIn(ExperimentalFoundationApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ImageDialog(
    url: String, animatedVisibilityScope: AnimatedVisibilityScope
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var imageSize by remember { mutableStateOf(IntSize.Zero) }

    val minScale = 1f
    val maxScale = 3f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
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
    ) {
        val interactionSource = remember { MutableInteractionSource() }

        AsyncImage(model = ImageRequest.Builder(LocalContext.current).data(url)
            .placeholderMemoryCacheKey(url).memoryCacheKey(url).build(),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .sharedBounds(
                    rememberSharedContentState(key = url),
                    animatedVisibilityScope = animatedVisibilityScope,
//                    boundsTransform = { _, _ -> tween(durationMillis = 1000) }
                )
                .onSizeChanged { imageSize = it }
                .combinedClickable(onDoubleClick = {
                    if (scale >= 2f) {
                        scale = 1f
                        offsetX = 0f
                        offsetY = 0f
                        return@combinedClickable
                    }
                    scale = 2f
                }, interactionSource = interactionSource, indication = null) {},
            contentDescription = "Content image",
            contentScale = ContentScale.FillWidth,
            placeholder = ASYNC_IMAGE_PLACEHOLDER
        )
    }
}