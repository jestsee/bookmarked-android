package com.example.bookmarked_android.ui.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.bookmarked_android.maxCharacters
import com.example.bookmarked_android.model.Author
import com.example.bookmarked_android.ui.theme.ASYNC_IMAGE_PLACEHOLDER
import com.example.bookmarked_android.ui.theme.Primary

@Composable
fun AuthorCard(author: Author) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
    ) {
        Divider(modifier = Modifier.weight(1f))
        Text(
            text = author.username.maxCharacters(20),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(0.8f)
        )
        Spacer(
            modifier = Modifier
                .padding(horizontal = 2.dp)
                .size(5.dp)
                .background(Primary, shape = CircleShape)
        )
        Text(
            text = author.name.maxCharacters(24), fontSize = 14.sp, fontWeight = FontWeight.Medium
        )
        AsyncImage(
            modifier = Modifier
                .size(24.dp)
                .clip(shape = RoundedCornerShape(50)),
            model = author.avatar,
            contentDescription = "Author's avatar",
            placeholder = ASYNC_IMAGE_PLACEHOLDER,
        )
    }
}
