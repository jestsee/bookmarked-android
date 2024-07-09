package com.example.bookmarked_android.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.bookmarked_android.R

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    value: String,
    onChange: (String) -> Unit,
    onClear: () -> Unit,
    trailing: @Composable (() -> Unit)?,
    leading: @Composable (() -> Unit)? = {
        Icon(
            modifier = Modifier.size(20.dp),
            painter = painterResource(id = R.drawable.icon_search),
            contentDescription = "Search icon"
        )
    },
    shape: RoundedCornerShape = RoundedCornerShape(50)
) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val heightInDp = with(LocalDensity.current) { size.height.toDp() }

    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(heightInDp),
            contentColor = MaterialTheme.colorScheme.inverseOnSurface,
            content = {},
            tonalElevation = 1.dp,
            shape = shape,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.inverseSurface.copy(.075f))
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    size = coordinates.size
                },
            value = value,
            onValueChange = onChange,
            placeholder = { Text("Search") },
            leadingIcon = leading,
            trailingIcon = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .height(IntrinsicSize.Min)
                ) {
                    if (value.isNotEmpty()) {
                        IconButton(onClick = onClear) {
                            Icon(
                                contentDescription = "Clear",
                                imageVector = Icons.Rounded.Clear,
                            )
                        }
                    }
                    trailing.let {
                        VerticalDivider(
                            modifier = Modifier.padding(vertical = 4.dp),
                            color = MaterialTheme.colorScheme.inverseSurface.copy(.1f),
                        )
                        trailing?.invoke()
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