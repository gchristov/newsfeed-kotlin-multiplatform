package com.gchristov.newsfeed.kmmfeed

import com.gchristov.newsfeed.kmmcommonmvvmtest.CommonViewModelTestClass
import com.gchristov.newsfeed.kmmcommontest.FakeResponse
import com.gchristov.newsfeed.kmmfeeddata.model.Feed
import com.gchristov.newsfeed.kmmfeedtestfixtures.FakeFeedRepository
import com.gchristov.newsfeed.kmmfeedtestfixtures.FeedCreator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.*

@ExperimentalCoroutinesApi
class FeedViewModelTest : CommonViewModelTestClass() {

    private lateinit var feedRepository: FakeFeedRepository

    @Test
    fun initialLoadingSetsState() = runTest(feedResponse = FakeResponse.LoadsForever) { viewModel ->
        assertTrue { viewModel.state.value.loading }
        assertFalse { viewModel.state.value.loadingMore }
    }

    @Test
    fun redecoratingSetsContent() = runTest { viewModel ->
        // Take a non-favourite post and mark as favourite through the repository
        val post = viewModel.state.value.feed!!.posts.first().copy(favouriteTimestamp = null)
        feedRepository.toggleFavourite(post.post.uid)
        viewModel.redecorateContent()

        assertNotNull(viewModel.state.value.feed!!.posts.first().favouriteTimestamp)
    }

    @Test
    fun refreshingContentClearsCacheAndReloads() = runTest { viewModel ->
        feedRepository.resetCurrentPage()
        feedRepository.feedResponse = FakeResponse.LoadsForever
        viewModel.refreshContent()

        feedRepository.assertCacheCleared()
        assertTrue { viewModel.state.value.loading }
    }

    @Test
    fun loadNextPageReachesEnd() = runTest { viewModel ->
        viewModel.loadNextPage(startFromFirst = false)

        assertTrue { viewModel.state.value.reachedEnd }
    }

    @Test
    fun loadNextPageLoadsMore() = runTest(feed = FeedCreator.multiPageFeed()) { viewModel ->
        feedRepository.feedLoadMoreResponse = FakeResponse.LoadsForever
        viewModel.loadNextPage(startFromFirst = false)

        assertFalse { viewModel.state.value.loading }
        assertTrue { viewModel.state.value.loadingMore }
        assertFalse { viewModel.state.value.reachedEnd }
        assertNull(viewModel.state.value.blockingError)
        assertNull(viewModel.state.value.nonBlockingError)
    }

    @Test
    fun onLoadSuccessSetsCorrectState() {
        val feeds = FeedCreator.singlePageFeed()
        val feed = feeds.first()

        runTest(feed = feeds) { viewModel ->
            assertFalse { viewModel.state.value.loading }
            assertFalse { viewModel.state.value.loadingMore }
            assertEquals(
                expected = feed,
                actual = viewModel.state.value.feed
            )
        }
    }

    @Test
    fun onLoadErrorSetsCorrectState() {
        val errorMessage = "Error message"
        val response = FakeResponse.Error(errorMessage)

        runTest(feedResponse = response) { viewModel ->
            assertFalse { viewModel.state.value.loading }
            assertFalse { viewModel.state.value.loadingMore }
            assertEquals(
                expected = errorMessage,
                actual = viewModel.state.value.blockingError?.message
            )
            assertNull(viewModel.state.value.nonBlockingError)
        }
    }

    @Test
    fun onLoadMoreErrorSetsCorrectState() {
        val errorMessage = "Error message"
        val response = FakeResponse.Error(errorMessage)

        runTest(feed = FeedCreator.multiPageFeed()) { viewModel ->
            feedRepository.feedLoadMoreResponse = response
            viewModel.loadNextPage(startFromFirst = false)

            assertFalse { viewModel.state.value.loading }
            assertFalse { viewModel.state.value.loadingMore }
            assertNull(viewModel.state.value.blockingError)
            assertEquals(
                expected = errorMessage,
                actual = viewModel.state.value.nonBlockingError?.message
            )
        }
    }

    @Test
    fun dismissingNonBlockingErrorHidesIt() {
        val response = FakeResponse.Error("Error message")

        runTest(feed = FeedCreator.multiPageFeed()) { viewModel ->
            feedRepository.feedLoadMoreResponse = response
            viewModel.loadNextPage(startFromFirst = false)

            assertNotNull(viewModel.state.value.nonBlockingError)
            viewModel.dismissNonBlockingError()
            assertNull(viewModel.state.value.nonBlockingError)
        }
    }

    private fun runTest(
        feed: List<Feed> = FeedCreator.singlePageFeed(),
        feedResponse: FakeResponse = FakeResponse.CompletesNormally,
        feedLoadMoreResponse: FakeResponse = FakeResponse.CompletesNormally,
        block: (FeedViewModel) -> Unit
    ) {
        feedRepository = FakeFeedRepository(feed = feed).apply {
            this.feedResponse = feedResponse
            this.feedLoadMoreResponse = feedLoadMoreResponse
        }
        val viewModel = FeedViewModel(feedRepository = feedRepository)
        block(viewModel)
    }
}
