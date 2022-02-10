package com.gchristov.newsfeed.commoncompose.elements

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import com.gchristov.newsfeed.commoncompose.theme.Theme

@Composable
fun AppScreen(
    topBar: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = topBar,
        backgroundColor = Theme.backgrounds.primary,
    ) {
        content()
    }
}