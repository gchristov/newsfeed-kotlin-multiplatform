package com.gchristov.newsfeed.multiplatform.post.feature

import com.gchristov.newsfeed.multiplatform.common.mvvmtest.CommonViewModelTestClass
import com.gchristov.newsfeed.multiplatform.common.test.FakeCoroutineDispatcher
import com.gchristov.newsfeed.multiplatform.common.test.FakeResponse
import com.gchristov.newsfeed.multiplatform.post.data.Post
import com.gchristov.newsfeed.multiplatform.post.testfixtures.FakePostRepository
import com.gchristov.newsfeed.multiplatform.post.testfixtures.PostCreator
import com.gchristov.newsfeed.multiplatform.post.testfixtures.PostCreator.PostId
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
    fun contentLoadSetsLoadingState() {
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
    fun contentLoadSetsCacheState() {
        // Given
        val cache = PostCreator.post()
        val response = FakeResponse.LoadsForever

        // When
        runTest(
            postCache = cache,
            postResponse = response
        ) { viewModel, postRepository ->
            // Then
            assertTrue { viewModel.state.value.loading }
            assertEquals(
                expected = cache.id,
                actual = viewModel.state.value.postId
            )
            assertEquals(
                expected = cache,
                actual = viewModel.state.value.post?.raw
            )
            assertNull(viewModel.state.value.blockingError)
        }
    }

    @Test
    fun contentLoadSetsSuccessState() {
        // Given
        val post = PostCreator.post()
        // When
        runTest(post = post) { viewModel, _ ->
            // Then
            assertFalse { viewModel.state.value.loading }
            assertEquals(
                expected = post.id,
                actual = viewModel.state.value.postId
            )
            assertEquals(
                expected = post,
                actual = viewModel.state.value.post?.raw
            )
            assertNull(viewModel.state.value.blockingError)
        }
    }

    @Test
    fun contentLoadSetsErrorState() {
        // Given
        val post = PostCreator.post()
        val errorMessage = "Error message"
        val response = FakeResponse.Error(errorMessage)
        // When
        runTest(
            post = post,
            postResponse = response
        ) { viewModel, _ ->
            // Then
            assertFalse { viewModel.state.value.loading }
            assertEquals(
                expected = post.id,
                actual = viewModel.state.value.postId
            )
            assertNull(viewModel.state.value.post)
            assertEquals(
                expected = errorMessage,
                actual = viewModel.state.value.blockingError?.message
            )
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
        assertEquals(PostId, viewModel.state.value.postId)
        assertNull(viewModel.state.value.blockingError)
        assertNull(viewModel.state.value.post)
    }

    @Test
    fun toggleFavourite() {
        // Given
        val post = PostCreator.post()
        runTest(post = post) { viewModel, _ ->
            // When
            viewModel.onToggleFavourite()
            // Then
            assertNotNull(viewModel.state.value.post?.favouriteTimestamp)
            // When
            viewModel.onToggleFavourite()
            // Then
            assertNull(viewModel.state.value.post?.favouriteTimestamp)
        }
    }

    private fun runTest(
        post: Post = PostCreator.post(),
        postCache: Post? = null,
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
        )
        testBlock(viewModel, postRepository)
    }
}