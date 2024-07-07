package com.example.bookmarked_android.ui.screens.bookmarks

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class BookmarkTypeEnum {
    Tweet, Thread
}

val bookmarkTypes = BookmarkTypeEnum.entries.map { it }

class FilterTypeViewModel: ViewModel() {
    private val _selectedType = MutableStateFlow<String?>(null)
    val selectedType: StateFlow<String?> = _selectedType.asStateFlow()

    fun selectType(type: BookmarkTypeEnum) {
        _selectedType.value = type.name
    }

    fun deselectType() {
        _selectedType.value = null
    }
}