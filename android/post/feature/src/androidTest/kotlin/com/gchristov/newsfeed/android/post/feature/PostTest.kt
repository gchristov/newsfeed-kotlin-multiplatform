package com.gchristov.newsfeed.android.post.feature

import com.gchristov.newsfeed.android.post.testfixtures.PostRobot
import com.gchristov.newsfeed.android.post.testfixtures.post
import com.gchristov.newsfeed.android.common.composetest.CommonComposeTestClass
import com.gchristov.newsfeed.multiplatform.common.test.FakeCoroutineDispatcher
import com.gchristov.newsfeed.multiplatform.common.test.FakeResponse
import com.gchristov.newsfeed.multiplatform.post.data.model.DecoratedPost
import com.gchristov.newsfeed.multiplatform.post.data.usecase.DecoratePostUseCase
import com.gchristov.newsfeed.multiplatform.post.data.usecase.RealDecoratePostUseCase
import com.gchristov.newsfeed.multiplatform.post.feature.PostViewModel
import com.gchristov.newsfeed.multiplatform.post.testfixtures.FakePostRepository
import com.gchristov.newsfeed.multiplatform.post.testfixtures.PostCreator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@ExperimentalCoroutinesApi
class PostTest : CommonComposeTestClass() {

    @Test
    fun loadingIndicatorShown() {
        // Given
        val response = FakeResponse.LoadsForever
        // When
        runTest(postResponse = response) {
            // Then
            assertLoadingExists()
            assertAddToFavouritesButtonDoesNotExist()
            assertRemoveFromFavouritesButtonDoesNotExist()
            assertPostDoesNotExist(
                title = PostTitle,
                author = PostAuthor,
                readingTime = PostReadingTime,
                body = PostBody
            )
            assertBlockingErrorDoesNotExist()
        }
    }

    @Test
    fun cacheShown() {
        // Given
        val cache = PostCreator.post(favouriteTimestamp = null)
        val response = FakeResponse.LoadsForever
        // When
        runTest(
            postCache = cache,
            postResponse = response
        ) {
            // Then
            assertLoadingExists()
            assertAddToFavouritesButtonExists()
            assertRemoveFromFavouritesButtonDoesNotExist()
            assertPostExists(
                title = PostTitle,
                author = PostAuthor,
                readingTime = PostReadingTime,
                body = PostBody
            )
            assertBlockingErrorDoesNotExist()
        }
    }

    @Test
    fun postShown() = runTest {
        assertLoadingDoesNotExist()
        assertAddToFavouritesButtonExists()
        assertRemoveFromFavouritesButtonDoesNotExist()
        assertPostExists(
            title = PostTitle,
            author = PostAuthor,
            readingTime = PostReadingTime,
            body = PostBody
        )
        assertBlockingErrorDoesNotExist()
    }

    @Test
    fun blockingErrorShown() {
        // Given
        val response = FakeResponse.Error()
        // When
        runTest(postResponse = response) {
            // Then
            assertLoadingDoesNotExist()
            assertAddToFavouritesButtonDoesNotExist()
            assertRemoveFromFavouritesButtonDoesNotExist()
            assertPostDoesNotExist(
                title = PostTitle,
                author = PostAuthor,
                readingTime = PostReadingTime,
                body = PostBody
            )
            assertBlockingErrorExists()
        }
    }

    @Test
    fun toggleFavouriteAddsToFavourites() {
        // Given
        val post = PostCreator.post(favouriteTimestamp = null)
        // When
        runTest(post = post) {
            // Then
            assertAddToFavouritesButtonExists()
            assertRemoveFromFavouritesButtonDoesNotExist()
            // When
            clickAddToFavouritesButton()
            // Then
            assertAddToFavouritesButtonDoesNotExist()
            assertRemoveFromFavouritesButtonExists()
        }
    }

    @Test
    fun toggleFavouriteRemovesFromFavourites() {
        // Given
        val post = PostCreator.post(favouriteTimestamp = 123L)
        // When
        runTest(post = post) {
            // Then
            assertAddToFavouritesButtonDoesNotExist()
            assertRemoveFromFavouritesButtonExists()
            // When
            clickRemoveFromFavouritesButton()
            // Then
            assertAddToFavouritesButtonExists()
            assertRemoveFromFavouritesButtonDoesNotExist()
        }
    }

    private fun runTest(
        post: DecoratedPost = PostCreator.post(favouriteTimestamp = null),
        postCache: DecoratedPost? = null,
        postResponse: FakeResponse = FakeResponse.CompletesNormally,
        testBlock: PostRobot.() -> Unit
    ) {
        // Setup test environment
        val postRepository = FakePostRepository(
            post = post,
            postCache = postCache
        ).apply {
            this.postResponse = postResponse
        }

        val decoratePostUseCase = RealDecoratePostUseCase(
            postRepository = postRepository,
            dispatcher = FakeCoroutineDispatcher
        )

        composeRule.setContent {
            val viewModel = PostViewModel(
                dispatcher = Dispatchers.Main,
                postId = PostId,
                postRepository = postRepository,
                decoratePostUseCase = decoratePostUseCase
            )
            PostScreen(viewModel)
        }
        composeRule.post(testBlock)
    }
}

private const val PostId = "post_123"
private const val PostTitle = "Post Title"
private const val PostAuthor = "Anonymous"
private const val PostReadingTime = "1 min read"
private const val PostBody = "This is a sample post body"