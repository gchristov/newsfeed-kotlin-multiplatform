package com.gchristov.newsfeed.feed

import android.content.Intent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.gchristov.newsfeed.commoncompose.CommonComposeActivity
import com.gchristov.newsfeed.commoncompose.elements.*
import com.gchristov.newsfeed.commoncompose.elements.avatar.AppAvatar
import com.gchristov.newsfeed.commoncompose.theme.Theme
import com.gchristov.newsfeed.commonnavigation.NavigationModule
import com.gchristov.newsfeed.kmmcommonmvvm.createViewModelFactory
import com.gchristov.newsfeed.kmmfeed.FeedModule
import com.gchristov.newsfeed.kmmfeed.FeedViewModel
import com.gchristov.newsfeed.kmmfeeddata.model.DecoratedPost
import com.gchristov.newsfeed.kmmfeeddata.model.Feed

class FeedActivity : CommonComposeActivity() {
    private val viewModel by viewModels<FeedViewModel> { createViewModelFactory { FeedModule.injectFeedViewModel() } }
    private val navigator by lazy { NavigationModule.injectNavigator(context = this) }

    @Composable
    override fun Content() = FeedScreen(
        viewModel = viewModel,
        onPostClick = { post ->
            navigator.openPost(postId = post.post.uid)
        }
    )

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        viewModel.loadNextPage()
    }

    override fun onResume() {
        super.onResume()
        viewModel.redecorateContent()
    }
}

@Composable
internal fun FeedScreen(
    viewModel: FeedViewModel,
    onPostClick: (post: DecoratedPost) -> Unit
) {
    val state = viewModel.state.ld().observeAsState().value

    when {
        state?.blockingError != null -> ErrorState(
            blockingError = state.blockingError!!.toUiBlockingError(),
            onRetry = viewModel::refreshContent
        )
        else -> FeedState(
            loading = state?.loading == true,
            reachedEnd = state?.reachedEnd == true,
            feed = state?.feed,
            nonBlockingError = state?.nonBlockingError?.toUiNonBlockingError(),
            onNonBlockingErrorDismiss = viewModel::dismissNonBlockingError,
            onRefresh = viewModel::refreshContent,
            onLoadMore = { viewModel.loadNextPage(startFromFirst = false) },
            onPostClick = onPostClick
        )
    }
}

@Composable
private fun FeedState(
    loading: Boolean,
    reachedEnd: Boolean,
    feed: Feed?,
    nonBlockingError: NonBlockingError?,
    onNonBlockingErrorDismiss: () -> Unit,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    onPostClick: (post: DecoratedPost) -> Unit
) {
    AppScreen(
        topBar = {
            AppBar(title = stringResource(R.string.app_name))
        },
    ) {
        AppPullRefresh(
            loading = loading,
            onRefresh = onRefresh
        ) {
            Box {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    feed?.let { feed ->
                        itemsIndexed(feed.posts) { index, post ->
                            PostRow(
                                post = post,
                                onPostClick = onPostClick
                            )
                            if (index == feed.posts.lastIndex
                                && !reachedEnd
                                && nonBlockingError == null
                            ) {
                                LoadMoreRow()
                                onLoadMore()
                            }
                        }
                    }
                }
                nonBlockingError?.let { nonBlockingError ->
                    AppNonBlockingError(
                        nonBlockingError = nonBlockingError,
                        onRetry = onLoadMore,
                        onDismiss = onNonBlockingErrorDismiss
                    )
                }
            }
        }
    }
}

@Composable
private fun PostRow(
    post: DecoratedPost,
    onPostClick: (post: DecoratedPost) -> Unit
) {
    AppListRow(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple()
            ) {
                onPostClick(post)
            }
            .fillMaxWidth(),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            PostHeader(post = post)
            post.post.body?.let { body ->
                AppText(
                    text = body,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun PostHeader(post: DecoratedPost) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AppText(
                modifier = Modifier
                    .weight(1f)
                    .alignBy(FirstBaseline),
                text = post.post.title,
                style = Theme.typography.title,
            )
            if (post.isFavourite()) {
                AppIcon(
                    modifier = Modifier.alignBy(FirstBaseline),
                    imageVector = Icons.Filled.Favorite,
                    tint = Theme.contentColors.action,
                    contentDescription = stringResource(R.string.feed_favourite)
                )
            }
        }
        PostAuthor(post.post.author)
    }
}

@Composable
private fun PostAuthor(author: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        AppAvatar(
            size = 24.dp,
            text = author
        )
        AppText(
            text = author,
            style = Theme.typography.subtitle,
            color = Theme.contentColors.secondary
        )
    }
}

@Composable
private fun LoadMoreRow() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        AppCircularProgressIndicator()
    }
}

@Composable
private fun ErrorState(
    blockingError: BlockingError,
    onRetry: () -> Unit
) {
    AppScreen(
        topBar = {
            AppBar(title = stringResource(R.string.app_name))
        },
    ) {
        AppBlockingError(
            blockingError = blockingError,
            onRetry = onRetry
        )
    }
}
