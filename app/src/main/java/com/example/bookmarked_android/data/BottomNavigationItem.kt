package com.example.bookmarked_android.data

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavigationItem(
    val title: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)