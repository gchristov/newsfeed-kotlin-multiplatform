package com.gchristov.newsfeed.multiplatform.feed.feature

import com.gchristov.newsfeed.kmmcommonmvvmtest.CommonViewModelTestClass
import com.gchristov.newsfeed.kmmcommontest.FakeClock
import com.gchristov.newsfeed.kmmcommontest.FakeCoroutineDispatcher
import com.gchristov.newsfeed.kmmcommontest.FakeResponse
import com.gchristov.newsfeed.multiplatform.feed.data.model.DecoratedFeedPage
import com.gchristov.newsfeed.multiplatform.feed.data.model.SectionedFeed
import com.gchristov.newsfeed.multiplatform.feed.data.usecase.BuildSectionedFeedUseCase
import com.gchristov.newsfeed.multiplatform.feed.data.usecase.FlattenSectionedFeedUseCase
import com.gchristov.newsfeed.multiplatform.feed.data.usecase.GetSectionedFeedUseCase
import com.gchristov.newsfeed.multiplatform.feed.data.usecase.MergeSectionedFeedUseCase
import com.gchristov.newsfeed.multiplatform.feed.data.usecase.RedecorateSectionedFeedUseCase
import com.gchristov.newsfeed.multiplatform.feed.testfixtures.FakeFeedRepository
import com.gchristov.newsfeed.multiplatform.feed.testfixtures.FeedCreator
import com.gchristov.newsfeed.multiplatform.post.testfixtures.FakePostRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class FeedViewModelTest : CommonViewModelTestClass() {

    //TODO: disabled as flaky
    @Ignore
    @Test
    fun searchQueryIsDebouncedOnExpectedInterval()  {
        runTest { viewModel, _, _ ->
            viewModel.onSearchTextChanged("te")
            delay(100)
            assertTrue { viewModel.state.value.searchQuery == "brexit,fintech" }

            viewModel.onSearchTextChanged("text")
            delay(200)
            assertTrue { viewModel.state.value.searchQuery == "brexit,fintech" }

            delay(400)
            assertTrue { viewModel.state.value.searchQuery == "text" }
        }
    }

    @Test
    fun initialLoadingSetsState() {
        // Given
        val response = FakeResponse.LoadsForever
        // When
        runTest(feedResponse = response) { viewModel, _, _ ->
            // Then
            assertTrue { viewModel.state.value.loading }
            assertFalse { viewModel.state.value.loadingMore }
            assertFalse { viewModel.state.value.reachedEnd }
            assertNull(viewModel.state.value.blockingError)
            assertNull(viewModel.state.value.nonBlockingError)
        }
    }

    @Test
    fun redecoratingSetsContent() = runTest { viewModel, _, postRepository ->
        // Given
        var post = viewModel.state.value.sectionedFeed!!.sections.first().feedItems.first()
        val prevTimestamp = post.favouriteTimestamp
        // When
        postRepository.toggleFavourite(post.raw.itemId)
        viewModel.redecorateContent()
        // Then
        post = viewModel.state.value.sectionedFeed!!.sections.first().feedItems.first()
        assertTrue { post.favouriteTimestamp != prevTimestamp }
    }

    @Test
    fun refreshingContentClearsCacheAndReloads() = runTest { viewModel, feedRepository, _ ->
        // Given
        feedRepository.resetCurrentPage()
        feedRepository.feedResponse = FakeResponse.LoadsForever
        // When
        viewModel.refreshContent()
        // Then
        feedRepository.assertCacheCleared()
        assertTrue { viewModel.state.value.loading }
        assertFalse { viewModel.state.value.loadingMore }
        assertFalse { viewModel.state.value.reachedEnd }
        assertNull(viewModel.state.value.blockingError)
        assertNull(viewModel.state.value.nonBlockingError)
    }

    @Test
    fun loadNextPageReachesEnd() {
        // Given
        val feed = FeedCreator.singlePageFeed()
        runTest(feedPages = feed) { viewModel, _, _ ->
            // When
            viewModel.loadNextPage(startFromFirst = false)
            // Then
            assertTrue { viewModel.state.value.reachedEnd }
        }
    }

    @Test
    fun loadNextPageLoadsMore() {
        // Given
        val feed = FeedCreator.multiPageFeed()
        runTest(feedPages = feed) { viewModel, feedRepository, _ ->
            feedRepository.feedLoadMoreResponse = FakeResponse.LoadsForever
            // When
            viewModel.loadNextPage(startFromFirst = false)
            // Then
            assertFalse { viewModel.state.value.loading }
            assertTrue { viewModel.state.value.loadingMore }
            assertFalse { viewModel.state.value.reachedEnd }
            assertNull(viewModel.state.value.blockingError)
            assertNull(viewModel.state.value.nonBlockingError)
        }
    }

    @Test
    fun onLoadSuccessSetsCache() {
        // Given
        val cache = FeedCreator.singlePageFeed().first()
        val response = FakeResponse.LoadsForever
        val expected = SectionedFeed(
            pages = 1,
            currentPage = 1,
            sections = listOf(
                SectionedFeed.Section(
                    type = SectionedFeed.SectionType.ThisWeek,
                    feedItems = cache.items.subList(0, 1)
                ),
                SectionedFeed.Section(
                    type = SectionedFeed.SectionType.LastWeek,
                    feedItems = cache.items.subList(1, 2)
                ),
                SectionedFeed.Section(
                    type = SectionedFeed.SectionType.ThisMonth,
                    feedItems = cache.items.subList(2, 3)
                ),
                SectionedFeed.Section(
                    type = SectionedFeed.SectionType.Older(Instant.parse(FeedCreator.DateOtherMonth)),
                    feedItems = cache.items.subList(3, 4)
                )
            )
        )
        // When
        runTest(
            feedPageCache = cache,
            feedResponse = response
        ) { viewModel, feedRepository, _ ->
            // Then
            feedRepository.assertCacheCleared()
            assertTrue { viewModel.state.value.loading }
            assertFalse { viewModel.state.value.loadingMore }
            assertEquals(
                expected = expected,
                actual = viewModel.state.value.sectionedFeed
            )
            assertNull(viewModel.state.value.blockingError)
            assertNull(viewModel.state.value.nonBlockingError)
        }
    }

    @Test
    fun onLoadSuccessSetsCorrectState() {
        // Given
        val feed = FeedCreator.singlePageFeed()
        val expected = SectionedFeed(
            pages = 1,
            currentPage = 1,
            sections = listOf(
                SectionedFeed.Section(
                    type = SectionedFeed.SectionType.ThisWeek,
                    feedItems = feed.first().items.subList(0, 1)
                ),
                SectionedFeed.Section(
                    type = SectionedFeed.SectionType.LastWeek,
                    feedItems = feed.first().items.subList(1, 2)
                ),
                SectionedFeed.Section(
                    type = SectionedFeed.SectionType.ThisMonth,
                    feedItems = feed.first().items.subList(2, 3)
                ),
                SectionedFeed.Section(
                    type = SectionedFeed.SectionType.Older(Instant.parse(FeedCreator.DateOtherMonth)),
                    feedItems = feed.first().items.subList(3, 4)
                )
            )
        )
        // When
        runTest(feedPages = feed) { viewModel, _, _ ->
            // Then
            assertFalse { viewModel.state.value.loading }
            assertFalse { viewModel.state.value.loadingMore }
            assertEquals(
                expected = expected,
                actual = viewModel.state.value.sectionedFeed
            )
            assertNull(viewModel.state.value.blockingError)
            assertNull(viewModel.state.value.nonBlockingError)
        }
    }

    @Test
    fun onLoadErrorSetsCorrectState() {
        // Given
        val errorMessage = "Error message"
        val response = FakeResponse.Error(errorMessage)
        // When
        runTest(feedResponse = response) { viewModel, _, _ ->
            // Then
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
        // Given
        val feed = FeedCreator.multiPageFeed()
        val errorMessage = "Error message"
        val response = FakeResponse.Error(errorMessage)
        runTest(feedPages = feed) { viewModel, feedRepository, _ ->
            // When
            feedRepository.feedLoadMoreResponse = response
            viewModel.loadNextPage(startFromFirst = false)
            // Then
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
        // Given
        val feed = FeedCreator.multiPageFeed()
        val response = FakeResponse.Error("Error message")
        runTest(feedPages = feed) { viewModel, feedRepository, _ ->
            // When
            feedRepository.feedLoadMoreResponse = response
            viewModel.loadNextPage(startFromFirst = false)
            // Then
            assertNotNull(viewModel.state.value.nonBlockingError)
            viewModel.dismissNonBlockingError()
            assertNull(viewModel.state.value.nonBlockingError)
        }
    }

    private fun runTest(
        feedPages: List<DecoratedFeedPage> = FeedCreator.singlePageFeed(),
        feedPageCache: DecoratedFeedPage? = null,
        feedResponse: FakeResponse = FakeResponse.CompletesNormally,
        feedLoadMoreResponse: FakeResponse = FakeResponse.CompletesNormally,
        testBlock: suspend CoroutineScope.(FeedViewModel, FakeFeedRepository, FakePostRepository) -> Unit
    ) = runBlocking {
        // Setup test environment
        val postRepository = FakePostRepository()
        val feedRepository = FakeFeedRepository(
            postRepository = postRepository,
            feedPages = feedPages,
            feedPageCache = feedPageCache,
        ).apply {
            this.feedResponse = feedResponse
            this.feedLoadMoreResponse = feedLoadMoreResponse
        }
        val getSectionedFeedUseCase = GetSectionedFeedUseCase(
            feedRepository = feedRepository,
            buildSectionedFeedUseCase = BuildSectionedFeedUseCase(
                dispatcher = FakeCoroutineDispatcher,
                clock = FakeClock
            ),
            mergeSectionedFeedUseCase = MergeSectionedFeedUseCase(dispatcher = FakeCoroutineDispatcher)
        )
        val redecorateSectionedFeedUseCase = RedecorateSectionedFeedUseCase(
            feedRepository = feedRepository,
            flattenSectionedFeedUseCase = FlattenSectionedFeedUseCase(dispatcher = FakeCoroutineDispatcher),
            buildSectionedFeedUseCase = BuildSectionedFeedUseCase(
                dispatcher = FakeCoroutineDispatcher,
                clock = FakeClock
            )
        )
        val viewModel = FeedViewModel(
            dispatcher = FakeCoroutineDispatcher,
            feedRepository = feedRepository,
            getSectionedFeedUseCase = getSectionedFeedUseCase,
            redecorateSectionedFeedUseCase = redecorateSectionedFeedUseCase
        )
        testBlock(viewModel, feedRepository, postRepository)
    }
}