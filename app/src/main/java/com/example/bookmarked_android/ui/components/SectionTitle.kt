package com.example.bookmarked_android.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.sp
import com.example.bookmarked_android.ui.theme.HORIZONTAL_PADDING

class SectionTitleProvider : PreviewParameterProvider<String> {
    override val values = sequenceOf("Section Title")
}

@Preview(showBackground = true)
@Composable
fun SectionTitlePreview(@PreviewParameter(SectionTitleProvider::class) title: String) {
    SectionTitle(title)
}

@Composable
fun SectionTitle(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = HORIZONTAL_PADDING),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Medium, letterSpacing = 0.sp)
        Text(
            text = "View more",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.sp,
            color = Color(0xFF7C68E2)
        )
    }
}