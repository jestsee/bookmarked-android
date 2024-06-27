package com.example.bookmarked_android

import android.annotation.SuppressLint
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.bookmarked_android.model.Tag
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun String.maxCharacters(max: Int): String {
    return if (this.length <= max) this
    else this.take(max - 3).plus("...")
}

fun List<Tag>.toJson(): String {
    val bookmarkTagsListType = object : TypeToken<List<Tag>>() {}.type
    val jsonTags = Gson().toJson(this, bookmarkTagsListType)
    return jsonTags
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