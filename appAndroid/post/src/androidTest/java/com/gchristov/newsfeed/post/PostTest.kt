package com.gchristov.newsfeed.post

import com.gchristov.newsfeed.commoncomposetest.createCustomComposeRule
import com.gchristov.newsfeed.kmmcommontest.FakeResponse
import com.gchristov.newsfeed.kmmfeeddata.model.DecoratedPost
import com.gchristov.newsfeed.kmmfeedtestfixtures.FakeFeedRepository
import com.gchristov.newsfeed.kmmfeedtestfixtures.PostCreator
import com.gchristov.newsfeed.kmmpost.PostViewModel
import com.gchristov.newsfeed.posttestfixtures.PostRobot
import com.gchristov.newsfeed.posttestfixtures.post
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PostTest {
    @get:Rule
    val composeRule = createCustomComposeRule()

    @Test
    fun loadingIndicatorShown() = runTest(postResponse = FakeResponse.LoadsForever) {
        assertLoadingExists()
        assertPostDoesNotExist(
            title = PostTitle,
            author = PostAuthor,
            readingTime = PostReadingTime,
            body = PostBody
        )
        assertBlockingErrorDoesNotExist()
    }

    @Test
    fun postShown() = runTest {
        assertLoadingDoesNotExist()
        assertPostExists(
            title = PostTitle,
            author = PostAuthor,
            readingTime = PostReadingTime,
            body = PostBody
        )
        assertBlockingErrorDoesNotExist()
    }

    @Test
    fun blockingErrorShown() = runTest(postResponse = FakeResponse.Error()) {
        assertLoadingDoesNotExist()
        assertPostDoesNotExist(
            title = PostTitle,
            author = PostAuthor,
            readingTime = PostReadingTime,
            body = PostBody
        )
        assertBlockingErrorExists()
    }

    @Test
    fun toggleFavouriteAddsToFavourites() =
        runTest(post = PostCreator.post(favouriteTimestamp = null)) {
            assertAddToFavouritesButtonExists()
            assertRemoveFromFavouritesButtonDoesNotExist()
            clickAddToFavouritesButton()
            assertAddToFavouritesButtonDoesNotExist()
            assertRemoveFromFavouritesButtonExists()
        }

    @Test
    fun toggleFavouriteRemovesFromFavourites() =
        runTest(post = PostCreator.post(favouriteTimestamp = 123L)) {
            assertAddToFavouritesButtonDoesNotExist()
            assertRemoveFromFavouritesButtonExists()
            clickRemoveFromFavouritesButton()
            assertAddToFavouritesButtonExists()
            assertRemoveFromFavouritesButtonDoesNotExist()
        }

    private fun runTest(
        post: DecoratedPost = PostCreator.post(favouriteTimestamp = null),
        postResponse: FakeResponse = FakeResponse.CompletesNormally,
        block: PostRobot.() -> Unit
    ) {
        val feedRepository = FakeFeedRepository(post = post).apply {
            this.postResponse = postResponse
        }
        composeRule.setContent {
            val viewModel = PostViewModel(
                postId = PostId,
                feedRepository = feedRepository
            )
            PostScreen(viewModel)
        }
        composeRule.post(block)
    }
}

private const val PostId = "post_123"
private const val PostTitle = "Post Title"
private const val PostAuthor = "steve"
private const val PostReadingTime = "1 min read"
private const val PostBody = "This is a sample post body"
