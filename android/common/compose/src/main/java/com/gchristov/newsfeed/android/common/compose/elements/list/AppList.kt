package com.gchristov.newsfeed.android.common.compose.elements.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gchristov.newsfeed.android.common.compose.theme.Theme

@Suppress("unused")
@Composable
fun AppList(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    showDividers: Boolean = true,
    content: AppListScope.() -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = state
    ) {
        RealAppListScope(
            lazyListScope = this,
            showDividers = showDividers
        ).apply(content)
    }
}

@LazyScopeMarker
interface AppListScope {
    fun items(
        count: Int,
        key: (index: Int) -> Any,
        itemContent: @Composable LazyItemScope.(index: Int) -> Unit
    )
}

@Suppress("unused")
inline fun <T> AppListScope.items(
    items: List<T>,
    noinline key: (item: T) -> Any,
    crossinline itemContent: @Composable LazyItemScope.(item: T) -> Unit
) = items(items.size, { index: Int -> key(items[index]) }) {
    itemContent(items[it])
}

private class RealAppListScope(
    private val lazyListScope: LazyListScope,
    private val showDividers: Boolean
) : AppListScope {
    private var itemCount = 0

    override fun items(
        count: Int,
        key: (index: Int) -> Any,
        itemContent: @Composable LazyItemScope.(index: Int) -> Unit
    ) {
        val firstItemIndex = itemCount
        itemCount += count

        lazyListScope.items(
            count = count,
            key = { index -> key(index) }
        ) { index ->
            this@RealAppListScope.Item(itemIndex = firstItemIndex + index) { itemContent(index) }
        }
    }

    @Composable
    private fun Item(
        itemIndex: Int,
        content: @Composable () -> Unit
    ) {
        val isAtLastRow = itemIndex == itemCount - 1

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Theme.backgrounds.surface)
        ) {
            content()

            if (showDividers && !isAtLastRow) {
                Divider(color = Theme.backgrounds.primary)
            }
        }
    }
}

@Composable
fun AppListRow(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(ListSpacing)
    ) {
        content()
    }
}

private val ListSpacing = 16.dp