package com.example.bookmarked_android.ui.screens.detail

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

val ROUNDED_CORNER_SHAPE = RoundedCornerShape(12.dp)

@Preview(showBackground = true)
@Composable
fun DetailLoading() {
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        for (i in 1..4)
            DetailLoadingText()
        DetailLoadingText(Modifier.fillMaxWidth(.25f))
        Spacer(modifier = Modifier.fillMaxWidth())
        DetailLoadingText()
        DetailLoadingText(Modifier.fillMaxWidth(.5f))
        Spacer(modifier = Modifier.fillMaxWidth())
        Box(
            Modifier
                .fillMaxWidth()
                .height(160.dp)
                .background(brush = loadingBrush(), shape = ROUNDED_CORNER_SHAPE)
        )
    }
}

@Composable
private fun DetailLoadingText(modifier: Modifier = Modifier) {
    Box(
        modifier
            .fillMaxWidth()
            .height(16.dp)
            .background(brush = loadingBrush(), shape = ROUNDED_CORNER_SHAPE)
    )
}

@Composable
fun loadingBrush(targetValue: Float = 1000f): Brush {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.15f),
        Color.LightGray.copy(alpha = 0.08f),
        Color.LightGray.copy(alpha = 0.15f),
    )

    val transition = rememberInfiniteTransition(label = "")
    val translateAnimation = transition.animateFloat(
        initialValue = 0f, targetValue = targetValue, animationSpec = infiniteRepeatable(
            animation = tween(800), repeatMode = RepeatMode.Reverse
        ), label = ""
    )
    return Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnimation.value, y = translateAnimation.value)
    )
}