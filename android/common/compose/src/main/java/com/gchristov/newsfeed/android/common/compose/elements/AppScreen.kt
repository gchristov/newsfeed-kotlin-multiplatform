package com.gchristov.newsfeed.android.common.compose.elements

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import com.gchristov.newsfeed.android.common.compose.theme.Theme

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