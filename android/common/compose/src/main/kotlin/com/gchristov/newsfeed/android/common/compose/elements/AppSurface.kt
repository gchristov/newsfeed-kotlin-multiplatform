package com.gchristov.newsfeed.android.common.compose.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gchristov.newsfeed.android.common.compose.theme.Theme

@Composable
fun AppSurface(
    modifier: Modifier = Modifier,
    elevation: Dp = 2.dp,
    content: @Composable () -> Unit
) {
    androidx.compose.material.Surface(
        modifier = modifier,
        color = Theme.backgrounds.surface,
        shape = Theme.shapes.surface,
        elevation = elevation
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            content()
        }
    }
}