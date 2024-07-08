package com.example.bookmarked_android.navigation

enum class Screen {
    HOME, BOOKMARK_LIST, BOOKMARK_DETAIL, IMAGE_DETAIL, PROFILE
}

sealed class NavigationItem(val route: String) {
    data object Home : NavigationItem(Screen.HOME.name)
    data object BookmarkList : NavigationItem(Screen.BOOKMARK_LIST.name)
    data object BookmarkDetail :
        NavigationItem(Screen.BOOKMARK_DETAIL.name + "/{bookmarkId}/{params}")

    data object ImageDetail :
        NavigationItem(Screen.IMAGE_DETAIL.name + "/{imageUrl}")

    data object Profile : NavigationItem(Screen.PROFILE.name)
}