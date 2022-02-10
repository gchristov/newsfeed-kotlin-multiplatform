package com.gchristov.newsfeed.kmmpost

import com.gchristov.newsfeed.kmmcommonmvvmtest.CommonViewModelTestClass
import com.gchristov.newsfeed.kmmcommontest.FakeResponse
import com.gchristov.newsfeed.kmmfeeddata.model.DecoratedPost
import com.gchristov.newsfeed.kmmfeedtestfixtures.FakeFeedRepository
import com.gchristov.newsfeed.kmmfeedtestfixtures.PostCreator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.*

@ExperimentalCoroutinesApi
class PostViewModelTest : CommonViewModelTestClass() {

    private lateinit var feedRepository: FakeFeedRepository

    @Test
    fun initialLoadingStateSet() = runTest(postResponse = FakeResponse.LoadsForever) { viewModel ->
        assertTrue { viewModel.state.value.loading }
    }

    @Test
    fun resetPostReloadsContent() = runTest { viewModel ->
        feedRepository.postResponse = FakeResponse.LoadsForever
        viewModel.resetPostId(PostId)

        assertTrue { viewModel.state.value.loading }
    }

    @Test
    fun onLoadSuccessSetsCorrectState() {
        val post = PostCreator.post(favouriteTimestamp = null)

        runTest(post = post) { viewModel ->
            assertFalse { viewModel.state.value.loading }
            assertEquals(
                expected = post,
                actual = viewModel.state.value.post
            )
        }
    }

    @Test
    fun onLoadErrorSetsCorrectState() {
        val errorMessage = "Error message"
        val response = FakeResponse.Error(errorMessage)

        runTest(postResponse = response) { viewModel ->
            assertFalse { viewModel.state.value.loading }
            assertEquals(
                expected = errorMessage,
                actual = viewModel.state.value.blockingError?.message
            )
        }
    }

    @Test
    fun toggleFavouriteAddsToFavourites() =
        runTest(post = PostCreator.post(favouriteTimestamp = null)) { viewModel ->
            viewModel.onToggleFavourite()

            assertNotNull(viewModel.state.value.post?.favouriteTimestamp)
        }

    @Test
    fun toggleFavouriteRemovesFromFavourites() =
        runTest(post = PostCreator.post(favouriteTimestamp = 123L)) { viewModel ->
            viewModel.onToggleFavourite()

            assertNull(viewModel.state.value.post?.favouriteTimestamp)
        }

    private fun runTest(
        post: DecoratedPost = PostCreator.post(favouriteTimestamp = null),
        postResponse: FakeResponse = FakeResponse.CompletesNormally,
        block: (viewModel: PostViewModel) -> Unit
    ) {
        feedRepository = FakeFeedRepository(post = post).apply {
            this.postResponse = postResponse
        }
        val viewModel = PostViewModel(
            postId = PostId,
            feedRepository = feedRepository
        )
        block(viewModel)
    }
}

private const val PostId = "post_123"