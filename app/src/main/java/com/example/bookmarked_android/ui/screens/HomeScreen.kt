package com.example.bookmarked_android.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookmarked_android.BottomNavigationBar
import com.example.bookmarked_android.ui.components.SectionTitle


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun HomeScreen() {
    Scaffold(
        bottomBar = { BottomNavigationBar() }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            GreetingsText()
//            TODO: search bar
            SectionTitle(title = "Featured")
            Card(onClick = { /*TODO*/ }) {
                Text(text = "ini card")
            }
            SectionTitle(title = "Recently bookmarked")
        }
    }
}

@Composable
fun GreetingsText() {
    Column {
        Text(text = "Hello name", fontSize = 36.sp, fontWeight = FontWeight.Medium)
        Text(text = "You have bookmarked 140 tweets", fontSize = 16.sp)
    }
}