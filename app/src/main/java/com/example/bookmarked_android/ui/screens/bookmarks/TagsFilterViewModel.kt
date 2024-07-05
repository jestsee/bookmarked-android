package com.example.bookmarked_android.ui.screens.bookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmarked_android.Config
import com.example.bookmarked_android.model.Tag
import com.example.bookmarked_android.network.NotionApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TagsFilterViewModel : ViewModel() {
    private val config = Config()

    private val _selectedTags = MutableStateFlow<List<Tag>>(emptyList())
    val selectedTags: StateFlow<List<Tag>> = _selectedTags.asStateFlow()

    private val _tagOptions = MutableStateFlow<List<Tag>>(emptyList())
    val tagOptions: StateFlow<List<Tag>> = _tagOptions.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        fetchTags()
    }

    private fun fetchTags() {
        viewModelScope.launch {
            _error.value = null
            _isLoading.value = true

            try {
                val response = NotionApi.retrofitService.getTags(
                    "Bearer ${config.notionSecret}",
                    config.databaseId
                )
                _tagOptions.value = response.results
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}