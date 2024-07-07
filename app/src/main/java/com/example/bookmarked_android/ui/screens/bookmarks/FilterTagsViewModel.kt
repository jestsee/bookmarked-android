package com.example.bookmarked_android.ui.screens.bookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookmarked_android.Config
import com.example.bookmarked_android.model.Tag
import com.example.bookmarked_android.network.NotionApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class TagOption(val tag: Tag, val isSelected: Boolean)

class FilterTagsViewModel : ViewModel() {
    private val config = Config()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow() // TODO

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var allTags = MutableStateFlow(emptyList<TagOption>())

    val tagOptions: StateFlow<List<TagOption>>
        get() = allTags.combine(searchQuery) { tags, query ->
            tags.filter { it.tag.name.contains(query, ignoreCase = true) }
        }.stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(), emptyList())

    val selectedTags: StateFlow<List<TagOption>>
        get() = allTags.map { list ->
            list.filter {
                it.isSelected
            }
        }.stateIn(viewModelScope, started = SharingStarted.Eagerly, emptyList())

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
                allTags.value = response.results.map { TagOption(it, false) }.toMutableList()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun updateSelectedValueTag(tag: TagOption, value: Boolean) {
        allTags.value = allTags.value.map {
            if (it.tag.id == tag.tag.id) {
                return@map it.copy(isSelected = value)
            }
            it
        }
    }

    fun toggleTag(tag: TagOption) {
        updateSelectedValueTag(tag, !tag.isSelected)
    }

    fun deselectAllTags() {
        allTags.value = allTags.value.map {
            it.copy(isSelected = false)
        }.toMutableList()
    }

    fun searchTags(query: String) {
        _searchQuery.value = query
    }

    fun getSelectedTags(): List<String> {
        return allTags.value.filter { it.isSelected  }.map { it.tag.name }
    }
}