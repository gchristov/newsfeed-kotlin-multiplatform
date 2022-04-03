package com.gchristov.newsfeed.post

import android.content.Intent
import androidx.activity.viewModels
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gchristov.newsfeed.commoncompose.CommonComposeActivity
import com.gchristov.newsfeed.commoncompose.elements.*
import com.gchristov.newsfeed.commoncompose.elements.avatar.AppAvatar
import com.gchristov.newsfeed.commoncompose.theme.Theme
import com.gchristov.newsfeed.kmmcommonmvvm.createViewModelFactory
import com.gchristov.newsfeed.kmmpost.PostModule
import com.gchristov.newsfeed.kmmpost.PostViewModel
import com.gchristov.newsfeed.kmmpostdata.model.DecoratedPost

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

    AppScreen {
        Box {
            AppPullRefresh(
                loading = loading,
                onRefresh = onRefresh
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState),
                ) {
                    post?.let { post ->
                        PostImage(post.raw.thumbnail)
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            PostHeader(post.raw.headline ?: "--", post.readingTimeMinutes ?: 1)
                            PostBody(post.raw.body ?: "--")
                        }
                    }
                }
            }
            PostAppBar(
                scrollState = scrollState,
                allowFavourite = post != null,
                isFavourite = post?.isFavourite() == true,
                onToggleFavourite = onToggleFavourite
            )
        }
    }
}

@Composable
private fun PostAppBar(
    scrollState: ScrollState,
    allowFavourite: Boolean,
    isFavourite: Boolean,
    onToggleFavourite: () -> Unit,
) {
    val isScrolled = scrollState.value != 0

    val appBarGradientColors = listOf(
        Theme.backgrounds.surface.copy(alpha = 0.7f),
        Color.Transparent
    )
    val appBarIcon = if (isFavourite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder
    val appBarIconContentDescription =
        stringResource(if (isFavourite) R.string.post_remove_favourite else R.string.post_add_favourite)

    Box(
        modifier = Modifier
            .background(brush = Brush.verticalGradient(colors = appBarGradientColors))
    ) {
        AppBar(
            showBack = true,
            elevated = isScrolled,
            actions = if (allowFavourite) {
                {
                    AppIconButton(
                        onClick = onToggleFavourite,
                        icon = appBarIcon,
                        contentDescription = appBarIconContentDescription,
                    )
                }
            } else null
        )
    }
}

@Composable
private fun PostImage(url: String?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(Theme.backgrounds.surface)
    ) {
        url?.let {
            AppImage(
                modifier = Modifier.fillMaxSize(),
                imageUrl = it,
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
private fun PostHeader(header: String, readingTimeMinutes: Int) {
    AppText(
        text = header,
        style = Theme.typography.title,
    )
    PostAuthor("Anonymous", readingTimeMinutes)
}

@Composable
private fun PostAuthor(author: String, readingTimeMinutes: Int) {
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
                text = stringResource(R.string.post_read_time, readingTimeMinutes),
                style = Theme.typography.caption,
                color = Theme.contentColors.secondary
            )
        }
    }
}

@Composable
private fun PostBody(text: String) {
    AppHtmlText(
        modifier = Modifier.padding(top = 16.dp),
        html = text
    )
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

private fun DecoratedPost.isFavourite() = favouriteTimestamp != null

private const val KeyPostId = "postId"
