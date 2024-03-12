package com.gchristov.newsfeed.android.common.compose.elements

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun AppPullRefresh(
    modifier: Modifier = Modifier,
    loading: Boolean,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit,
) {
    SwipeRefresh(
        modifier = modifier,
        state = rememberSwipeRefreshState(loading),
        onRefresh = onRefresh,
        content = content
    )
}
