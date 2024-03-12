package com.gchristov.newsfeed.android.common.compose.elements

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.gchristov.newsfeed.android.common.compose.theme.Theme

@Composable
fun AppCircularProgressIndicator(modifier: Modifier = Modifier) {
    androidx.compose.material.CircularProgressIndicator(
        modifier = modifier,
        color = Theme.contentColors.action,
    )
}