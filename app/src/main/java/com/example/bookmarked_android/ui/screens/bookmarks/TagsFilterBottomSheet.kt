package com.example.bookmarked_android.ui.screens.bookmarks

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookmarked_android.R
import com.example.bookmarked_android.ui.theme.HORIZONTAL_PADDING

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun TagsFilterBottomSheet(
    tagViewModel: FilterTagsViewModel,
    onDismissRequest: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val tagOptions by tagViewModel.tagOptions.collectAsState()

    ModalBottomSheet(
        modifier = Modifier.heightIn(min = 750.dp),
        onDismissRequest = onDismissRequest,
        sheetState = sheetState
    ) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = HORIZONTAL_PADDING),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(text = "Tags", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            }
            item {
                val searchValue by tagViewModel.searchQuery.collectAsState()
                Surface(
                    tonalElevation = 6.dp,
                    shape = RoundedCornerShape(25)
                ) {
                    TextField(
                        value = searchValue,
                        onValueChange = tagViewModel::searchTags,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Search tags") },
                        leadingIcon = {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(id = R.drawable.icon_search),
                                contentDescription = "Search tag"
                            )
                        },
                        trailingIcon = {
                            if (searchValue.isNotEmpty()) {
                                IconButton(onClick = { tagViewModel.searchTags("") }) {
                                    Icon(
                                        contentDescription = "Clear",
                                        imageVector = Icons.Rounded.Clear,
                                    )
                                }
                            }
                        },
                        colors = TextFieldDefaults.colors(
                            disabledTextColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent
                        )
                    )
                }
            }
            item {
                FlowRow(
                    modifier = Modifier.fillMaxHeight(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy((-6).dp),
                ) {
                    tagOptions.forEach {
                            FilterChip(
                                modifier = Modifier.customAnimatePlacement().padding(0.dp),
                                shape = RoundedCornerShape(50),
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                                ),
                                elevation = FilterChipDefaults.filterChipElevation(elevation = 20.dp),
                                border = BorderStroke(0.dp, Color.Transparent),
                                selected = it.isSelected,
                                onClick = { tagViewModel.toggleTag(it) },
                                label = { Text(it.tag.name, fontSize = 16.sp) },
                            )
//                        }
                    }
                }
            }
        }
    }
}