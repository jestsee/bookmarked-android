package com.example.bookmarked_android.ui.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.bookmarked_android.ui.theme.Purple

@Composable
fun OpenInButton(iconPainter: Int, platform: String, url: String) {
    val uriHandler = LocalUriHandler.current
    IconButton(
        onClick = { uriHandler.openUri(url) },
        Modifier
            .background(Purple.copy(.15f), shape = RoundedCornerShape(20))
            .padding(2.dp)
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(iconPainter),
            contentDescription = "Open in $platform",
            tint = Purple.copy(.8f)
        )
    }
}