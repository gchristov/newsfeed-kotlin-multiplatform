package com.gchristov.newsfeed.post

import android.content.Intent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gchristov.newsfeed.commoncompose.CommonComposeActivity
import com.gchristov.newsfeed.commoncompose.elements.*
import com.gchristov.newsfeed.commoncompose.elements.avatar.AppAvatar
import com.gchristov.newsfeed.commoncompose.theme.Theme
import com.gchristov.newsfeed.kmmcommonmvvm.createViewModelFactory
import com.gchristov.newsfeed.kmmfeeddata.Post
import com.gchristov.newsfeed.kmmfeeddata.model.DecoratedPost
import com.gchristov.newsfeed.kmmpost.PostModule
import com.gchristov.newsfeed.kmmpost.PostViewModel

class PostActivity : CommonComposeActivity() {
    private val viewModel by viewModels<PostViewModel> {
        createViewModelFactory {
            PostModule.injectPostViewModel(postId = postId)
        }
    }
    private val postId: String
        get() {
            return requireNotNull(intent.data?.getQueryParameter(KeyPostId))
        }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        viewModel.resetPostId(postId)
    }

    @Composable
    override fun Content() = PostScreen(viewModel)
}

@Composable
internal fun PostScreen(viewModel: PostViewModel) {
    val state = viewModel.state.ld().observeAsState().value

    when {
        state?.blockingError != null -> ErrorState(
            blockingError = state.blockingError!!.toUiBlockingError(),
            onRetry = viewModel::loadContent
        )
        else -> PostState(
            loading = state?.loading == true,
            post = state?.post,
            onRefresh = viewModel::loadContent,
            onToggleFavourite = viewModel::onToggleFavourite
        )
    }
}

@Composable
private fun PostState(
    loading: Boolean,
    post: DecoratedPost?,
    onRefresh: () -> Unit,
    onToggleFavourite: () -> Unit
) {
    val scrollState = rememberScrollState()

    AppScreen(
        topBar = {
            AppBar(
                showBack = true,
                elevated = scrollState.value != 0,
                actions = {
                    val icon =
                        if (post?.isFavourite() == true) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder
                    val contentDescription =
                        stringResource(if (post?.isFavourite() == true) R.string.post_remove_favourite else R.string.post_add_favourite)
                    AppIconButton(
                        onClick = onToggleFavourite,
                        icon = icon,
                        contentDescription = contentDescription,
                    )
                }
            )
        },
    ) {
        AppPullRefresh(
            loading = loading,
            onRefresh = onRefresh
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                post?.let { post ->
                    PostHeader(post = post)
                    post.post.body?.let { body ->
                        AppText(
                            modifier = Modifier.padding(top = 16.dp),
                            text = body,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PostHeader(post: DecoratedPost) {
    AppText(
        text = post.post.title,
        style = Theme.typography.title,
    )
    PostAuthor(post.post.author)
}

@Composable
private fun PostAuthor(author: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        AppAvatar(
            size = 36.dp,
            text = author,
            textStyle = Theme.typography.title
        )
        Column {
            AppText(
                text = author,
                style = Theme.typography.subtitle,
            )
            AppText(
                text = stringResource(R.string.post_read_time),
                style = Theme.typography.caption,
                color = Theme.contentColors.secondary
            )
        }
    }
}

@Composable
private fun ErrorState(
    blockingError: BlockingError,
    onRetry: () -> Unit
) {
    AppScreen(
        topBar = {
            AppBar(
                elevated = false,
                showBack = true
            )
        },
    ) {
        AppBlockingError(
            blockingError = blockingError,
            onRetry = onRetry
        )
    }
}

@Preview
@Composable
private fun Preview() {
    PostState(
        loading = false,
        post = DecoratedPost(
            post = Post(
                uid = "123",
                author = "Author",
                title = "Title",
                body = "Body",
                pageId = null,
                nextPageId = null
            ),
            favouriteTimestamp = null
        ),
        onRefresh = {},
        onToggleFavourite = {}
    )
}

private const val KeyPostId = "postId"
