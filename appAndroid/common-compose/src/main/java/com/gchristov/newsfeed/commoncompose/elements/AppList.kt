package com.gchristov.newsfeed.commoncompose.elements

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AppListRow(
    modifier: Modifier,
    content: @Composable () -> Unit
) {
    AppSurface(
        modifier = modifier,
        content = content
    )
}