package com.example.bookmarked_android.ui.components

import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.bookmarked_android.ui.theme.jetbrainsMonofontFamily

@Composable
fun TextUrl(modifier: Modifier = Modifier, text: String, url: String, fontSize: TextUnit = 16.sp) {
    val uriHandler = LocalUriHandler.current
    val annotatedText = buildAnnotatedString {
        pushStringAnnotation(tag = "URL", annotation = url)

        withStyle(
            style = SpanStyle(
//                color = Primary.copy(.8f),
                color = Color(0xFF8E85FF),
                fontSize = fontSize,
                fontFamily = jetbrainsMonofontFamily
            )
        ) { append(text) }
        pop()
    }

    ClickableText(
        modifier = modifier,
        text = annotatedText,
        onClick = { offset ->
            annotatedText.getStringAnnotations(
                tag = "URL",
                start = offset,
                end = offset
            ).firstOrNull()?.let { annotation ->
                uriHandler.openUri(annotation.item)
            }
        }
    )
}