package com.example.bookmarked_android.ui.screens.bookmarks

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmarked_android.Config
import com.example.bookmarked_android.model.Tag
import com.example.bookmarked_android.network.NotionApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TagsFilterViewModel : ViewModel() {
    private val config = Config()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val allTags = MutableStateFlow(emptyList<Tag>())

    private val _tagOptions = MutableStateFlow(emptyList<Tag>())
    val tagOptions: StateFlow<List<Tag>> = _tagOptions.asStateFlow()

    private val _selectedTags = MutableStateFlow(mutableListOf<Tag>())
    val selectedTags: StateFlow<List<Tag>> = _selectedTags.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow() // TODO

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        fetchTags()
        observeQuery()
    }

    private fun observeQuery() {
        viewModelScope.launch {
            _searchQuery.collectLatest {
                _tagOptions.value =
                    allTags.value.filter { tag -> tag.name.contains(it, ignoreCase = true) }
            }
        }
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
                allTags.value = response.results
                _tagOptions.value = response.results
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectTag(tag: Tag) {
        _selectedTags.value.add(tag)
    }

    fun deselectTag(tag: Tag) {
        Log.d("TAG", "deselectTag: masok ${tag.name}")
        _selectedTags.value.remove(tag)
    }

    fun deselectAllTags() {
        _selectedTags.value.clear()
    }

    fun searchTags(query: String) {
        _searchQuery.value = query
    }
}