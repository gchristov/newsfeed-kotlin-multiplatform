package com.gchristov.newsfeed.multiplatform.feed.feature

import com.gchristov.newsfeed.multiplatform.common.mvvmtest.CommonViewModelTestClass
import com.gchristov.newsfeed.multiplatform.common.test.FakeClock
import com.gchristov.newsfeed.multiplatform.common.test.FakeCoroutineDispatcher
import com.gchristov.newsfeed.multiplatform.common.test.FakeResponse
import com.gchristov.newsfeed.multiplatform.feed.data.model.DecoratedFeedPage
import com.gchristov.newsfeed.multiplatform.feed.data.model.SectionedFeed
import com.gchristov.newsfeed.multiplatform.feed.data.usecase.RealBuildSectionedFeedUseCase
import com.gchristov.newsfeed.multiplatform.feed.data.usecase.RealFlattenSectionedFeedUseCase
import com.gchristov.newsfeed.multiplatform.feed.data.usecase.RealMergeSectionedFeedUseCase
import com.gchristov.newsfeed.multiplatform.feed.data.usecase.RealRedecorateSectionedFeedUseCase
import com.gchristov.newsfeed.multiplatform.feed.testfixtures.FakeFeedRepository
import com.gchristov.newsfeed.multiplatform.feed.testfixtures.FeedCreator
import com.gchristov.newsfeed.multiplatform.post.testfixtures.FakePostRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class FeedViewModelTest : CommonViewModelTestClass() {

    @Test
    fun contentLoadSetsLoadingState() {
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
    fun redecorateContentRedecoratesContent() = runTest { viewModel, _, postRepository ->
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
    fun refreshContentSetsLoadingState() = runTest { viewModel, feedRepository, _ ->
        // Given
        feedRepository.pageIndex = 0
        feedRepository.feedResponse = FakeResponse.LoadsForever
        // When
        viewModel.refreshContent()
        // Then
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
    fun contentLoadSetsCacheState() {
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
    fun contentLoadSetsSuccessState() {
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
    fun contentLoadSetsErrorState() {
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
    fun contentLoadMoreSetsErrorState() {
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
    fun dismissNonBlockingErrorDismissesIt() {
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

    @Test
    fun searchQueryIsDebounced()  {
        runTest { viewModel, feedRepository, _ ->
            viewModel.onSearchTextChanged("te")
            delay(FeedSearchDebounceIntervalMillis - 200)
            feedRepository.assertSearchQuery("brexit,fintech")

            viewModel.onSearchTextChanged("text")
            delay(FeedSearchDebounceIntervalMillis - 100)
            feedRepository.assertSearchQuery("brexit,fintech")

            viewModel.onSearchTextChanged("text")
            delay(FeedSearchDebounceIntervalMillis)
            feedRepository.assertSearchQuery("text")
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
        val buildSectionedFeedUseCase = RealBuildSectionedFeedUseCase(
            dispatcher = FakeCoroutineDispatcher,
            clock = FakeClock
        )
        val mergeSectionedFeedUseCase = RealMergeSectionedFeedUseCase(
            dispatcher = FakeCoroutineDispatcher
        )
        val redecorateSectionedFeedUseCase = RealRedecorateSectionedFeedUseCase(
            dispatcher = FakeCoroutineDispatcher,
            feedRepository = feedRepository,
            flattenSectionedFeedUseCase = RealFlattenSectionedFeedUseCase(dispatcher = FakeCoroutineDispatcher),
            buildSectionedFeedUseCase = buildSectionedFeedUseCase,
        )
        val viewModel = FeedViewModel(
            dispatcher = FakeCoroutineDispatcher,
            feedRepository = feedRepository,
            redecorateSectionedFeedUseCase = redecorateSectionedFeedUseCase,
            buildSectionedFeedUseCase = buildSectionedFeedUseCase,
            mergeSectionedFeedUseCase = mergeSectionedFeedUseCase,
        )
        testBlock(viewModel, feedRepository, postRepository)
    }
}