package com.gchristov.newsfeed.multiplatform.post.feature

import com.gchristov.newsfeed.multiplatform.common.mvvmtest.CommonViewModelTestClass
import com.gchristov.newsfeed.multiplatform.common.test.FakeCoroutineDispatcher
import com.gchristov.newsfeed.multiplatform.common.test.FakeResponse
import com.gchristov.newsfeed.multiplatform.post.data.model.DecoratedPost
import com.gchristov.newsfeed.multiplatform.post.data.usecase.DecoratePostUseCase
import com.gchristov.newsfeed.multiplatform.post.data.usecase.RealDecoratePostUseCase
import com.gchristov.newsfeed.multiplatform.post.testfixtures.FakePostRepository
import com.gchristov.newsfeed.multiplatform.post.testfixtures.PostCreator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class PostViewModelTest : CommonViewModelTestClass() {

    @Test
    fun initialLoadingSetsState() {
        // Given
        val response = FakeResponse.LoadsForever
        // When
        runTest(postResponse = response) { viewModel, _ ->
            // Then
            assertTrue { viewModel.state.value.loading }
            assertNull(viewModel.state.value.blockingError)
        }
    }

    @Test
    fun resetPostReloadsContent() = runTest { viewModel, postRepository ->
        // Given
        postRepository.postResponse = FakeResponse.LoadsForever
        // When
        viewModel.resetPostId(PostId)
        // Then
        assertTrue { viewModel.state.value.loading }
        assertNull(viewModel.state.value.blockingError)
    }

    @Test
    fun onLoadSuccessSetsCache() {
        // Given
        val cache = PostCreator.post(favouriteTimestamp = null)
        val response = FakeResponse.LoadsForever

        // When
        runTest(
            postCache = cache,
            postResponse = response
        ) { viewModel, postRepository ->
            // Then
            postRepository.assertCacheCleared()
            assertTrue { viewModel.state.value.loading }
            assertEquals(
                expected = cache,
                actual = viewModel.state.value.post
            )
            assertNull(viewModel.state.value.blockingError)
        }
    }

    @Test
    fun onLoadSuccessSetsCorrectState() {
        // Given
        val post = PostCreator.post(favouriteTimestamp = null)
        // When
        runTest(post = post) { viewModel, _ ->
            // Then
            assertFalse { viewModel.state.value.loading }
            assertEquals(
                expected = post,
                actual = viewModel.state.value.post
            )
            assertNull(viewModel.state.value.blockingError)
        }
    }

    @Test
    fun onLoadErrorSetsCorrectState() {
        // Given
        val errorMessage = "Error message"
        val response = FakeResponse.Error(errorMessage)
        // When
        runTest(postResponse = response) { viewModel, _ ->
            // Then
            assertFalse { viewModel.state.value.loading }
            assertEquals(
                expected = errorMessage,
                actual = viewModel.state.value.blockingError?.message
            )
        }
    }

    @Test
    fun toggleFavouriteAddsToFavourites() {
        // Given
        val post = PostCreator.post(favouriteTimestamp = null)
        runTest(post = post) { viewModel, _ ->
            // When
            viewModel.onToggleFavourite()
            // Then
            assertNotNull(viewModel.state.value.post?.favouriteTimestamp)
        }
    }

    @Test
    fun toggleFavouriteRemovesFromFavourites() {
        // Given
        val post = PostCreator.post(favouriteTimestamp = 123L)
        runTest(post = post) { viewModel, _ ->
            // When
            viewModel.onToggleFavourite()
            // Then
            assertNull(viewModel.state.value.post?.favouriteTimestamp)
        }
    }

    private fun runTest(
        post: DecoratedPost = PostCreator.post(favouriteTimestamp = null),
        postCache: DecoratedPost? = null,
        postResponse: FakeResponse = FakeResponse.CompletesNormally,
        testBlock: suspend CoroutineScope.(viewModel: PostViewModel, FakePostRepository) -> Unit
    ) = runBlocking {
        // Setup test environment
        val postRepository = FakePostRepository(
            post = post,
            postCache = postCache
        ).apply {
            this.postResponse = postResponse
        }
        val viewModel = PostViewModel(
            dispatcher = FakeCoroutineDispatcher,
            postId = PostId,
            postRepository = postRepository,
            decoratePostUseCase = RealDecoratePostUseCase(postRepository, FakeCoroutineDispatcher)
        )
        testBlock(viewModel, postRepository)
    }
}

private const val PostId = "post_123"