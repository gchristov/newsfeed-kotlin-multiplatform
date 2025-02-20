package com.gchristov.newsfeed.multiplatform.post.feature

import com.gchristov.newsfeed.multiplatform.common.mvvmtest.CommonViewModelTestClass
import com.gchristov.newsfeed.multiplatform.common.test.FakeCoroutineDispatcher
import com.gchristov.newsfeed.multiplatform.common.test.FakeResponse
import com.gchristov.newsfeed.multiplatform.post.data.Post
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
    fun contentLoadSetsLoadingState() = runTest(
        postResponse = FakeResponse.LoadsForever
    ) { viewModel, _ ->
        assertTrue { viewModel.state.value.loading }
        assertNull(viewModel.state.value.blockingError)
    }

    @Test
    fun contentLoadSetsCacheState() {
        val cache = PostCreator.post()
        val response = FakeResponse.LoadsForever

        runTest(
            post = cache,
            usePostForCache = true,
            postResponse = response
        ) { viewModel, postRepository ->
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
        val post = PostCreator.post()

        runTest(post = post) { viewModel, _ ->
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
        val post = PostCreator.post()
        val errorMessage = "Error message"
        val response = FakeResponse.Error(errorMessage)

        runTest(
            post = post,
            postResponse = response
        ) { viewModel, _ ->
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
        val postId = "post_345"
        postRepository.postResponse = FakeResponse.LoadsForever

        viewModel.resetPostId(postId)

        assertTrue { viewModel.state.value.loading }
        assertEquals(postId, viewModel.state.value.postId)
        assertNull(viewModel.state.value.blockingError)
        assertNull(viewModel.state.value.post)
    }

    @Test
    fun toggleFavouriteTogglesFavourite() {
        val post = PostCreator.post()

        runTest(post = post) { viewModel, _ ->
            viewModel.onToggleFavourite()

            assertNotNull(viewModel.state.value.post?.favouriteTimestamp)

            viewModel.onToggleFavourite()

            assertNull(viewModel.state.value.post?.favouriteTimestamp)
        }
    }

    private fun runTest(
        post: Post = PostCreator.post(),
        usePostForCache: Boolean = false,
        postResponse: FakeResponse = FakeResponse.CompletesNormally,
        testBlock: suspend CoroutineScope.(viewModel: PostViewModel, FakePostRepository) -> Unit
    ) = runBlocking {
        // Setup test environment
        val postRepository = FakePostRepository(
            post = post,
            usePostForCache = usePostForCache,
        ).apply {
            this.postResponse = postResponse
        }
        val viewModel = PostViewModel(
            dispatcher = FakeCoroutineDispatcher,
            postId = post.id,
            postRepository = postRepository,
        )
        testBlock(viewModel, postRepository)
    }
}