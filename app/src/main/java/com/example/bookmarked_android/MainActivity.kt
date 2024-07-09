package com.example.bookmarked_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.bookmarked_android.navigation.NavigationHost
import com.example.bookmarked_android.ui.theme.BookmarkedandroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            BookmarkedandroidTheme {
                NavigationHost()
            }
        }
    }
}
