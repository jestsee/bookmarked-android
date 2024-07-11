package com.example.bookmarked_android.ui.screens.bookmarks

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.Spring.StiffnessMediumLow
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookmarked_android.R
import com.example.bookmarked_android.ui.theme.BOTTOM_PADDING
import com.example.bookmarked_android.ui.theme.HORIZONTAL_PADDING
import com.example.bookmarked_android.ui.theme.Primary
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun FilterBottomSheet(
    onDismissRequest: () -> Unit,
    onApply: () -> Unit,
    onClickAddTag: () -> Unit,
    viewModel: FilterViewModel = viewModel(),
) {
    val selectedType by viewModel.selectedType.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
    ) {
        LazyColumn(
            modifier = Modifier.padding(horizontal = HORIZONTAL_PADDING),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                UpperSection(viewModel::resetFilter)
                HorizontalDivider()
            }

            item {
                Text("Type")
                Spacer(modifier = Modifier.height(2.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    bookmarkTypes.forEach {
                        val isTypeSelected = it.name == selectedType
                        FilterChip(
                            selected = isTypeSelected,
                            onClick = {
                                if (!isTypeSelected) viewModel.selectType(it) else viewModel.deselectType()
                            },
                            label = { Text(it.name, fontSize = 16.sp) })
                    }
                }
            }

            item {
                TagSection(viewModel.tagViewModel, onClickAddTag = onClickAddTag)
                Spacer(Modifier.height(4.dp))
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = BOTTOM_PADDING * .25f),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(vertical = 16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Primary.copy(.75f),
                            contentColor = Color.White
                        ),
                        onClick = {
                            viewModel.applyFilter()
                            onApply()
                        }
                    ) {
                        Text(text = "Apply", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        tonalElevation = 16.dp,
                        contentColor = Primary,
                    ) {
                        Button(
                            contentPadding = PaddingValues(vertical = 16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = Color.White.copy(.8f)
                            ),
                            onClick = {
                                viewModel.resetFilter()
                                viewModel.applyFilter()
                                onApply()
                            },
                        ) {
                            Text(text = "Reset", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

        }
    }
}

@Composable
@OptIn(
    ExperimentalLayoutApi::class,
)
private fun TagSection(tagsViewModel: FilterTagsViewModel, onClickAddTag: () -> Unit) {
//    val isLoading by tagsViewModel.isLoading.collectAsState()
//    val tagOptions by tagsViewModel.tagOptions.collectAsState()
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

    if (selectedTags.isEmpty()) {
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(15),
            contentPadding = PaddingValues(vertical = 12.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.inverseSurface.copy(.25f)),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.inverseSurface.copy(
                    .8f
                )
            ),
            onClick = onClickAddTag,
        ) {
            Icon(imageVector = Icons.Rounded.Add, contentDescription = "Add tag")
            Spacer(modifier = Modifier.width(12.dp))
            Text("Add tag", fontSize = 16.sp)
        }
    } else {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy((-4).dp)
        ) {
            selectedTags.forEach {
                key(it.tag.id) {
                    FilterChip(
                        modifier = Modifier.customAnimatePlacement(),
                        shape = RoundedCornerShape(50),
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color.DarkGray.copy(
                                .6f
                            )
                        ),
                        elevation = FilterChipDefaults.filterChipElevation(elevation = 20.dp),
                        border = BorderStroke(0.dp, Color.Transparent),
                        selected = it.isSelected,
                        onClick = { tagsViewModel.toggleTag(it) },
                        label = { Text(it.tag.name, fontSize = 16.sp) },
                        trailingIcon = {
                            Icon(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clickable { tagsViewModel.toggleTag(it) },
                                imageVector = Icons.Rounded.Clear,
                                contentDescription = "Clear ${it.tag.name} tag"
                            )
                        }
                    )
                }
            }
            FilledTonalIconButton(
                onClick = onClickAddTag,
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = Icons.Rounded.Add,
                    contentDescription = ""
                )
            }
        }
    }

}

@Composable
private fun UpperSection(onReset: () -> Unit) {
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
        TextButton(onClick = onReset) {
            Text("Reset", color = Primary, fontSize = 16.sp)
        }
    }
}

fun Modifier.customAnimatePlacement(): Modifier = composed {
    val scope = rememberCoroutineScope()
    var targetOffset by remember { mutableStateOf(IntOffset.Zero) }
    var animatable by remember {
        mutableStateOf<Animatable<IntOffset, AnimationVector2D>?>(null)
    }
    this
        .onPlaced {
            // Calculate the position in the parent layout
            targetOffset = it
                .positionInParent()
                .round()
        }
        .offset {
            // Animate to the new target offset when alignment changes.
            val anim = animatable ?: Animatable(targetOffset, IntOffset.VectorConverter)
                .also { animatable = it }
            if (anim.targetValue != targetOffset) {
                scope.launch {
                    anim.animateTo(targetOffset, spring(stiffness = StiffnessMediumLow))
                }
            }
            // Offset the child in the opposite direction to the targetOffset, and slowly catch
            // up to zero offset via an animation to achieve an overall animated movement.
            animatable?.let { it.value - targetOffset } ?: IntOffset.Zero
        }
}