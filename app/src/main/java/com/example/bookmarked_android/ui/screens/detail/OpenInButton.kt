package com.example.bookmarked_android.ui.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.bookmarked_android.ui.theme.Purple

@Composable
fun OpenInButton(iconPainter: Int) {
    IconButton(
        onClick = { /*TODO*/ },
        Modifier
            .background(Purple.copy(.15f), shape = RoundedCornerShape(20))
            .padding(2.dp)
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(iconPainter),
            contentDescription = "Open in X",
            tint = Purple.copy(.8f)
        )
    }
}