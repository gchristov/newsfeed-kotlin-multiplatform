package com.gchristov.newsfeed.android.common.compose.elements.list

import android.os.Parcelable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.gchristov.newsfeed.android.common.compose.elements.AppText
import com.gchristov.newsfeed.android.common.compose.theme.Theme
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Composable
fun AppGroupedList(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    showDividers: Boolean = true,
    content: AppGroupedListScope.() -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = state,
        contentPadding = PaddingValues(AppGroupedListSpacing)
    ) {
        RealAppGroupedListScope(
            lazyListScope = this,
            showDividers = showDividers
        ).content()
    }
}

@LazyScopeMarker
interface AppGroupedListScope {
    fun group(
        key: Any,
        header: @Composable (() -> String)? = null,
        content: AppGroupScope.() -> Unit
    )

    fun item(
        key: Any,
        content: @Composable () -> Unit
    )
}

@LazyScopeMarker
interface AppGroupScope {
    val groupKey: Any

    fun items(
        count: Int,
        key: (index: Int) -> Any,
        itemContent: @Composable LazyItemScope.(index: Int) -> Unit
    )
}

@Suppress("unused")
inline fun <T> AppGroupScope.items(
    items: List<T>,
    noinline key: (item: T) -> Any,
    crossinline itemContent: @Composable LazyItemScope.(item: T) -> Unit
) = items(items.size, { index: Int -> key(items[index]) }) {
    itemContent(items[it])
}

private class RealAppGroupedListScope(
    private val lazyListScope: LazyListScope,
    private val showDividers: Boolean
) : AppGroupedListScope {
    private var groupCount = 0

    override fun group(
        key: Any,
        header: @Composable (() -> String)?,
        content: AppGroupScope.() -> Unit
    ) {
        val isFirstGroup = groupCount == 0
        groupCount++

        lazyListScope.item(key = key) {
            if (header != null) {
                var topPadding = AppGroupedListSpacing / 2
                if (!isFirstGroup) {
                    topPadding += AppGroupedListSpacing
                }
                AppText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = topPadding,
                            start = AppGroupedListSpacing,
                            bottom = 12.dp,
                            end = AppGroupedListSpacing
                        ),
                    text = header(),
                    style = Theme.typography.subtitle,
                    color = Theme.contentColors.secondary
                )
            } else if (!isFirstGroup) {
                // If there's no header ensure there's still some spacing between the sections
                // except the first one
                Spacer(modifier = Modifier.size(AppGroupedListSpacing))
            }
        }
        RealAppGroupScope(
            groupKey = key,
            lazyListScope = lazyListScope,
            showDividers = showDividers
        ).apply(content)
    }

    override fun item(
        key: Any,
        content: @Composable () -> Unit
    ) {
        lazyListScope.item(key = key) {
            content()
        }
    }
}

private class RealAppGroupScope(
    override val groupKey: Any,
    private val lazyListScope: LazyListScope,
    private val showDividers: Boolean
) : AppGroupScope {
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
            key = { index -> AppCompositeKey(groupKey, key(index)) }
        ) { index ->
            this@RealAppGroupScope.Item(itemIndex = firstItemIndex + index) { itemContent(index) }
        }
    }

    @Composable
    private fun Item(
        itemIndex: Int,
        content: @Composable () -> Unit
    ) {
        val isAtLastRow = itemIndex == itemCount - 1
        val shape = when {
            itemCount == 1 -> Theme.shapes.groupSingle
            itemIndex == 0 -> Theme.shapes.groupStart
            isAtLastRow -> Theme.shapes.groupEnd
            else -> Theme.shapes.groupMiddle
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .background(Theme.backgrounds.surface)
        ) {
            content()

            if (showDividers && !isAtLastRow) {
                Divider(color = Theme.backgrounds.primary)
            }
        }
    }
}

@Parcelize
private data class AppCompositeKey(
    val groupKey: @RawValue Any,
    val itemKey: @RawValue Any
) : Parcelable

private val AppGroupedListSpacing = 16.dp
