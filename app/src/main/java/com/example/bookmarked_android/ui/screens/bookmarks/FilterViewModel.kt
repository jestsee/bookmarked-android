package com.example.bookmarked_android.ui.screens.bookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class BookmarkTypeEnum {
    Tweet, Thread
}

val bookmarkTypes = BookmarkTypeEnum.entries.map { it }

data class BookmarkFilter(val type: String? = null, val tags: List<String>? = null)

class FilterViewModel(val tagViewModel: FilterTagsViewModel) : ViewModel() {
    private val _appliedFilter = MutableStateFlow(BookmarkFilter())
    val appliedFilter: StateFlow<BookmarkFilter> = _appliedFilter.asStateFlow()

    private val _selectedType = MutableStateFlow<String?>(null)
    val selectedType: StateFlow<String?> = _selectedType.asStateFlow()

    fun selectType(type: BookmarkTypeEnum) {
        _selectedType.value = type.name
    }

    fun deselectType() {
        _selectedType.value = null
    }

    fun resetFilter() {
        tagViewModel.deselectAllTags()
        deselectType()
    }

    fun applyFilter() {
        _appliedFilter.value =
            BookmarkFilter(type = selectedType.value, tags = tagViewModel.getSelectedTags())
    }
}

class FilterViewModelFactory(private val filterTagViewModel: FilterTagsViewModel) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FilterViewModel(filterTagViewModel) as T
    }
}

fun BookmarkFilter.count(): Int {
    var count = 0
    if (type != null) count++
    if (!tags.isNullOrEmpty()) count ++
    return count
}