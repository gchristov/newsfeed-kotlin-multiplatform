package com.gchristov.newsfeed.feed

import com.gchristov.newsfeed.commoncomposetest.createCustomComposeRule
import com.gchristov.newsfeed.feedtestfixtures.FeedRobot
import com.gchristov.newsfeed.feedtestfixtures.feed
import com.gchristov.newsfeed.kmmcommontest.FakeResponse
import com.gchristov.newsfeed.kmmfeed.FeedViewModel
import com.gchristov.newsfeed.kmmfeeddata.model.DecoratedPost
import com.gchristov.newsfeed.kmmfeeddata.model.Feed
import com.gchristov.newsfeed.kmmfeedtestfixtures.FakeFeedRepository
import com.gchristov.newsfeed.kmmfeedtestfixtures.FeedCreator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class FeedTest {
    @get:Rule
    val composeRule = createCustomComposeRule()

    private lateinit var lastClickedPost: DecoratedPost

    @Test
    fun singlePageFeedLoadingIndicatorShown() = runTest(feedResponse = FakeResponse.LoadsForever) {
        assertLoadingExists()
        assertFeedItemDoesNotExist(
            title = Post1Title,
            author = Post1Author,
            body = Post1Body,
        )
        assertBlockingErrorDoesNotExist()
        assertNonBlockingErrorDoesNotExist()
    }

    @Test
    fun singlePageFeedShown() = runTest {
        assertLoadingDoesNotExist()
        assertFeedItemExists(
            title = Post1Title,
            author = Post1Author,
            body = Post1Body,
        )
        assertFeedItemExists(
            title = Post2Title,
            author = Post2Author,
            body = Post2Body,
        )
        assertFavouriteItemsShown(favouriteItems = 1)
        assertBlockingErrorDoesNotExist()
        assertNonBlockingErrorDoesNotExist()
    }

    @Test
    fun multiPageFeedLoadingIndicatorShown() = runTest(
        feed = FeedCreator.multiPageFeed(),
        feedLoadMoreResponse = FakeResponse.LoadsForever
    ) {
        assertLoadingExists()
        assertFeedItemExists(
            title = Post1Title,
            author = Post1Author,
            body = Post1Body,
        )
        assertBlockingErrorDoesNotExist()
        assertNonBlockingErrorDoesNotExist()
    }

    @Test
    fun multiPageFeedShown() = runTest(feed = FeedCreator.multiPageFeed()) {
        assertLoadingDoesNotExist()
        assertFeedItemExists(
            title = Post1Title,
            author = Post1Author,
            body = Post1Body,
        )
        assertFeedItemExists(
            title = Post2Title,
            author = Post2Author,
            body = Post2Body,
        )
        assertFeedItemExists(
            title = Post3Title,
            author = Post3Author,
            body = Post3Body,
        )
        assertFavouriteItemsShown(favouriteItems = 2)
        assertBlockingErrorDoesNotExist()
        assertNonBlockingErrorDoesNotExist()
    }

    @Test
    fun blockingErrorShown() = runTest(feedResponse = FakeResponse.Error()) {
        assertLoadingDoesNotExist()
        assertFeedItemDoesNotExist(
            title = Post1Title,
            author = Post1Author,
            body = Post1Body,
        )
        assertBlockingErrorExists()
        assertNonBlockingErrorDoesNotExist()
    }

    @Test
    fun nonBlockingErrorShown() = runTest(
        feed = FeedCreator.multiPageFeed(),
        feedLoadMoreResponse = FakeResponse.Error()
    ) {
        assertLoadingDoesNotExist()
        assertFeedItemExists(
            title = Post1Title,
            author = Post1Author,
            body = Post1Body,
        )
        assertBlockingErrorDoesNotExist()
        assertNonBlockingErrorExists()
    }

    @Test
    fun feedItemClickOpensPost() = runTest {
        val post = FeedCreator.singlePageFeed().first().posts.first()
        clickPost(post.post.title)
        assertEquals(lastClickedPost, post)
    }

    private fun runTest(
        feed: List<Feed> = FeedCreator.singlePageFeed(),
        feedResponse: FakeResponse = FakeResponse.CompletesNormally,
        feedLoadMoreResponse: FakeResponse = FakeResponse.CompletesNormally,
        block: FeedRobot.() -> Unit
    ) {
        val feedRepository = FakeFeedRepository(feed = feed).apply {
            this.feedResponse = feedResponse
            this.feedLoadMoreResponse = feedLoadMoreResponse
        }
        composeRule.setContent {
            val viewModel = FeedViewModel(feedRepository = feedRepository)
            FeedScreen(
                viewModel = viewModel,
                onPostClick = { lastClickedPost = it }
            )
        }
        composeRule.feed(block)
    }
}

private const val Post1Title = "Post 1 Title"
private const val Post1Author = "steve"
private const val Post1Body = "This is a sample post 1 body"
private const val Post2Title = "Post 2 Title"
private const val Post2Author = "amy"
private const val Post2Body =
    "This is a sample post 2 body that may be longer and even go on multiple lines"
private const val Post3Title = "Post 3 Title"
private const val Post3Author = "sarah"
private const val Post3Body = "This is a sample post 3 body"