package com.gchristov.newsfeed.android.common.compose.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppPullRefresh(
    modifier: Modifier = Modifier,
    loading: Boolean,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit,
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = loading,
        onRefresh = onRefresh,
    )

    Box(
        modifier = modifier.pullRefresh(pullRefreshState),
        contentAlignment = Alignment.TopCenter,
    ) {
        content()

        PullRefreshIndicator(
            refreshing = loading,
            state = pullRefreshState,
        )
    }
}
