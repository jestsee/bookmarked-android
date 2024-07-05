package com.example.bookmarked_android.ui.screens.bookmarks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookmarked_android.R
import com.example.bookmarked_android.ui.theme.HORIZONTAL_PADDING
import com.example.bookmarked_android.ui.theme.Primary
import com.example.bookmarked_android.utils.verticalScrollBar

val types = listOf("Tweet", "Thread")

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun FilterBottomSheet(
    onDismissRequest: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(),
    viewModel: TagsFilterViewModel = viewModel()
) {
    val spacerModifier = Modifier.height(12.dp)
    val tags = viewModel.tagOptions.collectAsState().value

    var isExpanded by remember { mutableStateOf(false) }

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = HORIZONTAL_PADDING),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            UpperSection()
            HorizontalDivider()
            Spacer(modifier = spacerModifier)

            /**
             * Type
             */
            Text("Type")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                types.forEach {
                    FilterChip(
                        selected = false,
                        onClick = { /*TODO*/ },
                        label = { Text(it, fontSize = 16.sp) })
                }
            }

            /**
             * Tags
             */
            Spacer(modifier = spacerModifier)
            Text("Tags")
            Spacer(modifier = Modifier.height(4.dp))
            ExposedDropdownMenuBox(
                expanded = isExpanded,
                onExpandedChange = { isExpanded = !isExpanded }) {
                val dropdownScrollState = rememberScrollState()

                Row(
                    modifier = Modifier.height(intrinsicSize = IntrinsicSize.Max),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                color = MaterialTheme.colorScheme.inverseSurface.copy(.05f),
                                shape = RoundedCornerShape(12.dp)
                            ),
                    ) {
                        TextField(
                            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable),
                            placeholder = { Text("Select tags") },
                            trailingIcon = {
                                IconButton(onClick = { isExpanded = !isExpanded }) {
                                    Icon(
                                        modifier = Modifier.size(20.dp),
                                        imageVector = if (!isExpanded) Icons.Rounded.KeyboardArrowDown else Icons.Rounded.KeyboardArrowUp,
                                        //                                    painter = painterResource(id = R.drawable.icon_search),
                                        contentDescription = "Search"
                                    )
                                }
                            },
                            value = "",
                            onValueChange = {},
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
                    IconButton(
                        modifier = Modifier
                            .background(
                                color = Primary.copy(.75f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .fillMaxHeight()
                            .aspectRatio(1f),
                        onClick = { /*TODO*/ }) {
                        Icon(
                            painterResource(id = R.drawable.icon_trash_bin),
                            contentDescription = "Clear tags",
                            modifier = Modifier.size(28.dp),
                        )
                    }
                }
                ExposedDropdownMenu(
                    modifier = Modifier.heightIn(0.dp, 300.dp).verticalScrollBar(dropdownScrollState),
                    scrollState = dropdownScrollState,
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false }) {
                    // TODO tags list
                    tags.forEach {
                        DropdownMenuItem(
                            text = { Text(it.name) },
                            onClick = { /*TODO*/ })
                    }
                }
            }
            Spacer(spacerModifier)
        }
    }
}

@Composable
private fun UpperSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                modifier = Modifier.size(28.dp),
                painter = painterResource(id = R.drawable.icon_filter),
                contentDescription = "Filter icon",
            )
            Text("Filters", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
        }
        TextButton(onClick = { /*TODO*/ }) {
            Text("Clear", color = Primary, fontSize = 16.sp)
        }
    }
}