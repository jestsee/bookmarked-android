package com.example.bookmarked_android.utils

import android.annotation.SuppressLint
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

fun String.maxCharacters(max: Int): String {
    return if (this.length <= max) this
    else this.take(max - 3).plus("...")
}

const val PERCENT = "|percent-symbol|"
const val PLUS = "|plus-symbol|"

fun urlEncoder(url: String): String {
    val replacedUrl = url.replace("%", PERCENT).replace("+", PLUS)
    return URLEncoder.encode(replacedUrl, StandardCharsets.UTF_8.toString())
}

fun urlDecoder(url: String): String {
    val decodedUrl = URLDecoder.decode(url, StandardCharsets.UTF_8.toString())
    return decodedUrl.replace(PERCENT, "%").replace(PLUS, "+")
}

fun LazyListState.isReachedTop(): Boolean {
    return firstVisibleItemIndex == 0 && firstVisibleItemScrollOffset == 0
}

@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.leftBorder(strokeWidth: Dp, color: Color) = composed(factory = {
    val density = LocalDensity.current
    val strokeWidthPx = density.run { strokeWidth.toPx() }

    Modifier.drawBehind {
        val height = size.height
        val strokeWidthHalf = strokeWidthPx / 2

        drawLine(
            color = color.copy(.75f),
            start = Offset(x = strokeWidthHalf, y = (strokeWidthPx * 1.5f) + 20.dp.toPx()),
            end = Offset(x = strokeWidthHalf, y = height - 12.dp.toPx()),
            strokeWidth = strokeWidthPx,
            cap = StrokeCap.Round // This sets the tip to be rounded
        )

        drawCircle(
            color = color,
            radius = strokeWidthPx * 1.5f,
            center = Offset(x = strokeWidthHalf, y = 12.dp.toPx())
        )
    }
})