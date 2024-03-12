package com.gchristov.newsfeed.android.feed.feature

import com.gchristov.newsfeed.commoncomposetest.CommonComposeTestClass
import com.gchristov.newsfeed.android.feed.testfixtures.FeedRobot
import com.gchristov.newsfeed.android.feed.testfixtures.feed
import com.gchristov.newsfeed.multiplatform.common.test.FakeClock
import com.gchristov.newsfeed.multiplatform.common.test.FakeCoroutineDispatcher
import com.gchristov.newsfeed.multiplatform.common.test.FakeResponse
import com.gchristov.newsfeed.multiplatform.feed.data.model.DecoratedFeedItem
import com.gchristov.newsfeed.multiplatform.feed.data.model.DecoratedFeedPage
import com.gchristov.newsfeed.multiplatform.feed.data.usecase.BuildSectionedFeedUseCase
import com.gchristov.newsfeed.multiplatform.feed.data.usecase.FlattenSectionedFeedUseCase
import com.gchristov.newsfeed.multiplatform.feed.data.usecase.GetSectionedFeedUseCase
import com.gchristov.newsfeed.multiplatform.feed.data.usecase.MergeSectionedFeedUseCase
import com.gchristov.newsfeed.multiplatform.feed.data.usecase.RedecorateSectionedFeedUseCase
import com.gchristov.newsfeed.multiplatform.feed.feature.FeedViewModel
import com.gchristov.newsfeed.multiplatform.feed.testfixtures.FakeFeedRepository
import com.gchristov.newsfeed.multiplatform.feed.testfixtures.FeedCreator
import com.gchristov.newsfeed.multiplatform.post.testfixtures.FakePostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class FeedTest : CommonComposeTestClass() {
    private lateinit var lastClickedItem: DecoratedFeedItem

    @Test
    fun emptyStateShown() {
        // Given
        val feed = FeedCreator.emptyFeed()
        // When
        runTest(feedPages = feed) {
            // Then
            assertLoadingDoesNotExist()
            assertEmptyStateExists()
            assertSectionDoesNotExist(Post1Section)
            assertFeedItemDoesNotExist(
                title = Post1Title,
                date = Post1Date,
            )
            assertBlockingErrorDoesNotExist()
            assertNonBlockingErrorDoesNotExist()
        }
    }

    @Test
    fun cacheShown() {
        // Given
        val cache = FeedCreator.singlePageFeed().first()
        val response = FakeResponse.LoadsForever
        // When
        runTest(
            feedPageCache = cache,
            feedResponse = response
        ) {
            // Then
            assertLoadingExists()
            assertEmptyStateDoesNotExist()
            assertSectionExists(Post1Section)
            assertFeedItemExists(
                title = Post1Title,
                date = Post1Date,
            )
            assertSectionExists(Post2Section)
            assertFeedItemExists(
                title = Post2Title,
                date = Post2Date,
            )
            assertSectionExists(Post3Section)
            assertFeedItemExists(
                title = Post3Title,
                date = Post3Date,
            )
            assertSectionExists(Post4Section)
            assertFeedItemExists(
                title = Post4Title,
                date = Post4Date,
            )
            assertFavouriteItemsShown(favouriteItems = 2)
            assertBlockingErrorDoesNotExist()
            assertNonBlockingErrorDoesNotExist()
        }
    }

    @Test
    fun singlePageFeedLoadingIndicatorShown() {
        // Given
        val response = FakeResponse.LoadsForever
        // When
        runTest(feedResponse = response) {
            // Then
            assertLoadingExists()
            assertEmptyStateDoesNotExist()
            assertSectionDoesNotExist(Post1Section)
            assertFeedItemDoesNotExist(
                title = Post1Title,
                date = Post1Date,
            )
            assertBlockingErrorDoesNotExist()
            assertNonBlockingErrorDoesNotExist()
        }
    }

    @Test
    fun singlePageFeedShown() = runTest {
        assertLoadingDoesNotExist()
        assertEmptyStateDoesNotExist()
        assertSectionExists(Post1Section)
        assertFeedItemExists(
            title = Post1Title,
            date = Post1Date,
        )
        assertSectionExists(Post2Section)
        assertFeedItemExists(
            title = Post2Title,
            date = Post2Date,
        )
        assertSectionExists(Post3Section)
        assertFeedItemExists(
            title = Post3Title,
            date = Post3Date,
        )
        assertSectionExists(Post4Section)
        assertFeedItemExists(
            title = Post4Title,
            date = Post4Date,
        )
        assertFavouriteItemsShown(favouriteItems = 2)
        assertBlockingErrorDoesNotExist()
        assertNonBlockingErrorDoesNotExist()
    }

    @Test
    fun multiPageFeedLoadingIndicatorShown() {
        // Given
        val feed = FeedCreator.multiPageFeed()
        val response = FakeResponse.LoadsForever
        // When
        runTest(
            feedPages = feed,
            feedLoadMoreResponse = response
        ) {
            // Then
            assertLoadingExists()
            assertEmptyStateDoesNotExist()
            assertSectionExists(Post1Section)
            assertFeedItemExists(
                title = Post1Title,
                date = Post1Date,
            )
            assertBlockingErrorDoesNotExist()
            assertNonBlockingErrorDoesNotExist()
        }
    }

    @Test
    fun multiPageFeedShown() {
        // Given
        val feed = FeedCreator.multiPageFeed()
        // When
        runTest(feedPages = feed) {
            // Then
            assertLoadingDoesNotExist()
            assertEmptyStateDoesNotExist()
            assertSectionExists(Post1Section)
            assertFeedItemExists(
                title = Post1Title,
                date = Post1Date,
            )
            assertSectionExists(Post2Section)
            assertFeedItemExists(
                title = Post2Title,
                date = Post2Date,
            )
            assertSectionExists(Post3Section)
            assertFeedItemExists(
                title = Post3Title,
                date = Post3Date,
            )
            assertSectionExists(Post4Section)
            assertFeedItemExists(
                title = Post4Title,
                date = Post4Date,
            )
            assertFavouriteItemsShown(favouriteItems = 2)
            assertBlockingErrorDoesNotExist()
            assertNonBlockingErrorDoesNotExist()
        }
    }

    @Test
    fun blockingErrorShown() {
        // Given
        val response = FakeResponse.Error()
        // When
        runTest(feedResponse = response) {
            // Then
            assertLoadingDoesNotExist()
            assertEmptyStateDoesNotExist()
            assertSectionDoesNotExist(Post1Section)
            assertFeedItemDoesNotExist(
                title = Post1Title,
                date = Post1Date,
            )
            assertBlockingErrorExists()
            assertNonBlockingErrorDoesNotExist()
        }
    }

    @Test
    fun nonBlockingErrorShown() {
        // Given
        val feed = FeedCreator.multiPageFeed()
        val response = FakeResponse.Error()
        // When
        runTest(
            feedPages = feed,
            feedLoadMoreResponse = response
        ) {
            // Then
            assertLoadingDoesNotExist()
            assertEmptyStateDoesNotExist()
            assertSectionExists(Post1Section)
            assertFeedItemExists(
                title = Post1Title,
                date = Post1Date,
            )
            assertBlockingErrorDoesNotExist()
            assertNonBlockingErrorExists()
        }
    }

    @Test
    fun feedItemClickOpensPost() {
        // Given
        val post = FeedCreator.singlePageFeed().first().items.first()
        runTest {
            // When
            clickPost(requireNotNull(post.raw.headline))
            // Then
            assertEquals(lastClickedItem, post)
        }
    }

    private fun runTest(
        feedPages: List<DecoratedFeedPage> = FeedCreator.singlePageFeed(),
        feedPageCache: DecoratedFeedPage? = null,
        feedResponse: FakeResponse = FakeResponse.CompletesNormally,
        feedLoadMoreResponse: FakeResponse = FakeResponse.CompletesNormally,
        testBlock: FeedRobot.() -> Unit
    ) {
        // Setup test environment
        val postRepository = FakePostRepository()
        val feedRepository = FakeFeedRepository(
            postRepository = postRepository,
            feedPages = feedPages,
            feedPageCache = feedPageCache
        ).apply {
            this.feedResponse = feedResponse
            this.feedLoadMoreResponse = feedLoadMoreResponse
        }
        val buildSectionedFeedUseCase = BuildSectionedFeedUseCase(
            dispatcher = FakeCoroutineDispatcher,
            clock = FakeClock
        )
        val getSectionedFeedUseCase = GetSectionedFeedUseCase(
            feedRepository = feedRepository,
            buildSectionedFeedUseCase = buildSectionedFeedUseCase,
            mergeSectionedFeedUseCase = MergeSectionedFeedUseCase(dispatcher = FakeCoroutineDispatcher)
        )
        val redecorateSectionedFeedUseCase = RedecorateSectionedFeedUseCase(
            feedRepository = feedRepository,
            flattenSectionedFeedUseCase = FlattenSectionedFeedUseCase(dispatcher = FakeCoroutineDispatcher),
            buildSectionedFeedUseCase = buildSectionedFeedUseCase
        )
        composeRule.setContent {
            val viewModel = FeedViewModel(
                dispatcher = Dispatchers.Main,
                feedRepository = feedRepository,
                getSectionedFeedUseCase = getSectionedFeedUseCase,
                redecorateSectionedFeedUseCase = redecorateSectionedFeedUseCase
            )
            FeedScreen(
                viewModel = viewModel,
                feedItemDateFormatter = { ms -> FeedItemDateFormat.format(ms) },
                feedSectionDateFormatter = { ms -> FeedSectionDateFormat.format(ms) },
                onFeedItemClick = { lastClickedItem = it }
            )
        }
        composeRule.feed(testBlock)
    }
}

private const val Post1Section = "This week"
private const val Post1Title = "Post 1 Title"
private const val Post1Date = "21/02/2022"
private const val Post2Section = "Last week"
private const val Post2Title =
    "This is a sample post 2 title that may be longer and even go on multiple lines"
private const val Post2Date = "13/02/2022"
private const val Post3Section = "This month"
private const val Post3Title = "Post 3 Title"
private const val Post3Date = "01/02/2022"
private const val Post4Section = "Jan, 2022"
private const val Post4Title = "Post 4 Title"
private const val Post4Date = "01/01/2022"

private val FeedItemDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
private val FeedSectionDateFormat = SimpleDateFormat("MMM, yyyy", Locale.getDefault())