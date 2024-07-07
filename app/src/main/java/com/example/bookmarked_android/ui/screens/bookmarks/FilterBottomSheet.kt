package com.example.bookmarked_android.ui.screens.bookmarks

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.ModalBottomSheet
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

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun FilterBottomSheet(
    onDismissRequest: () -> Unit,
    tagsViewModel: FilterTagsViewModel = viewModel(),
    filterTypeViewModel: FilterTypeViewModel = viewModel(),
) {
    val spacerModifier = Modifier.height(12.dp)

    val selectedType by filterTypeViewModel.selectedType.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

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
                bookmarkTypes.forEach {
                    val isTypeSelected = it.name == selectedType
                    FilterChip(
                        selected = isTypeSelected,
                        onClick = {
                            if (!isTypeSelected) filterTypeViewModel.selectType(it) else filterTypeViewModel.deselectType()
                        },
                        label = { Text(it.name, fontSize = 16.sp) })
                }
            }
            Spacer(modifier = spacerModifier)
            TagSection(tagsViewModel)
            Spacer(spacerModifier)
        }
    }
}

@Composable
@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class,
)
private fun TagSection(tagsViewModel: FilterTagsViewModel) {
    var isExpanded by remember { mutableStateOf(false) }
    val isLoading by tagsViewModel.isLoading.collectAsState()
    val tagOptions by tagsViewModel.tagOptions.collectAsState()
    val selectedTags by tagsViewModel.selectedTags.collectAsState()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Tags")
        if (selectedTags.isNotEmpty()) {
            Text(
                "(${selectedTags.size} selected)",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.inverseSurface.copy(.5f)
            )
        }
    }
    Spacer(modifier = Modifier.height(4.dp))
    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { value -> isExpanded = value }) {
        val dropdownScrollState = rememberScrollState()

        Row(
            modifier = Modifier.height(intrinsicSize = IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TextField(
                readOnly = isLoading,
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.SecondaryEditable)
                    .weight(1f)
                    .background(
                        color = MaterialTheme.colorScheme.inverseSurface.copy(.05f),
                        shape = RoundedCornerShape(12.dp)
                    ),
                placeholder = { Text("Select tags") },
                trailingIcon = {
                    IconButton(
                        onClick = { isExpanded = !isExpanded }) {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            imageVector = if (!isExpanded) Icons.Rounded.KeyboardArrowDown else Icons.Rounded.KeyboardArrowUp,
                            contentDescription = "Search"
                        )
                    }
                },
                leadingIcon = if (isLoading) {
                    {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.5.dp
                        )
                    }
                } else null,
                value = tagsViewModel.searchQuery.collectAsState().value,
                onValueChange = tagsViewModel::searchTags,
                colors = TextFieldDefaults.colors(
                    disabledTextColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent
                )
            )
            IconButton(
                modifier = Modifier
                    .background(
                        color = Primary.copy(.75f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .fillMaxHeight()
                    .aspectRatio(1f),
                onClick = tagsViewModel::deselectAllTags
            ) {
                Icon(
                    painterResource(id = R.drawable.icon_trash_bin),
                    contentDescription = "Clear tags",
                    modifier = Modifier.size(28.dp),
                )
            }
        }
        if (tagOptions.isNotEmpty()) {
            ExposedDropdownMenu(
                modifier = Modifier
                    .heightIn(0.dp, 300.dp)
                    .verticalScrollBar(dropdownScrollState),
                shape = RoundedCornerShape(12.dp),
                scrollState = dropdownScrollState,
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false }) {
                tagOptions.forEach { tagOption ->
                    val toggleSelectedTag = { tagsViewModel.toggleSelectedTag(tagOption) }
                    DropdownMenuItem(
                        contentPadding = PaddingValues(horizontal = 24.dp),
                        text = { Text(tagOption.tag.name) },
                        onClick = toggleSelectedTag,
                        trailingIcon = {
                            Checkbox(
                                checked = tagOption.isSelected,
                                onCheckedChange = { toggleSelectedTag() }
                            )
                        }
                    )
                }
            }
        }
    }
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(-8.dp)
    ) {
        selectedTags.forEach {
            InputChip(
                selected = false,
                onClick = { /*TODO*/ },
                label = { Text(it.tag.name, fontSize = 16.sp) },
                trailingIcon = {
                    Icon(
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { tagsViewModel.deselectTag(it) },
                        imageVector = Icons.Rounded.Clear,
                        contentDescription = "Clear ${it.tag.name} tag"
                    )
                }
            )
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
            Text("Reset", color = Primary, fontSize = 16.sp)
        }
    }
}