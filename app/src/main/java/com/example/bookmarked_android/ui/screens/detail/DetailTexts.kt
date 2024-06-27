package com.example.bookmarked_android.ui.screens.detail

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.example.bookmarked_android.model.TextsContent
import com.example.bookmarked_android.ui.theme.Purple
import com.example.bookmarked_android.ui.theme.jetbrainsMonofontFamily

@Composable
fun DetailTexts(content: TextsContent, isTitle: Boolean = false) {
    val uriHandler = LocalUriHandler.current
    val spanStyle = SpanStyle(
        fontFamily = jetbrainsMonofontFamily,
        fontSize = if (isTitle) 32.sp else 16.sp,
        fontWeight = if (isTitle) FontWeight.Bold else FontWeight.Normal
    )

    val texts = content.texts.toMutableList()

    while (texts.isNotEmpty() && (texts.first().text == "" || texts.first().text == "\n")) {
        texts.removeFirst()
    }

    if (texts.isEmpty()) return

    val annotatedText = buildAnnotatedString {
        texts.forEach {
            // skip empty line at the end of the paragraph
            if (it.text == "\n" && it == content.texts.last()) return@forEach

            if (it.url != null) {
                pushStringAnnotation(tag = "URL", annotation = it.url)
                withStyle(
                    style = spanStyle.copy(
                        color = Purple
                    )
                ) { append(it.text) }

                pop()
                return@forEach
            }

            withStyle(style = spanStyle) {
                append(it.text)
            }
        }
    }

    ClickableText(style = TextStyle(color = MaterialTheme.colorScheme.onBackground).copy(lineHeight = if (isTitle) 32.sp else 26.sp),
        text = annotatedText,
        onClick = { offset ->
            annotatedText.getStringAnnotations(
                tag = "URL", start = offset, end = offset
            ).firstOrNull()?.let { annotation ->
                uriHandler.openUri(annotation.item)
            }
        })
}