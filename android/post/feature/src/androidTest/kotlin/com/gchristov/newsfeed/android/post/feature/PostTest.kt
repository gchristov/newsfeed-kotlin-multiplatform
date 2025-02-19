package com.gchristov.newsfeed.android.post.feature

import com.gchristov.newsfeed.android.common.composetest.CommonComposeTestClass
import com.gchristov.newsfeed.android.post.testfixtures.PostRobot
import com.gchristov.newsfeed.android.post.testfixtures.post
import com.gchristov.newsfeed.multiplatform.common.test.FakeResponse
import com.gchristov.newsfeed.multiplatform.post.data.Post
import com.gchristov.newsfeed.multiplatform.post.feature.PostViewModel
import com.gchristov.newsfeed.multiplatform.post.testfixtures.FakePostRepository
import com.gchristov.newsfeed.multiplatform.post.testfixtures.PostCreator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@ExperimentalCoroutinesApi
class PostTest : CommonComposeTestClass() {

    @Test
    fun loadingIndicatorShown() = runTest(postResponse = FakeResponse.LoadsForever) {
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

    @Test
    fun cacheShown() = runTest(
        usePostForCache = true,
        postResponse = FakeResponse.LoadsForever
    ) {
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
    fun blockingErrorShown() = runTest(postResponse = FakeResponse.Error()) {
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

    @Test
    fun toggleFavouriteTogglesFavourite() = runTest {
        assertAddToFavouritesButtonExists()
        assertRemoveFromFavouritesButtonDoesNotExist()

        clickAddToFavouritesButton()

        assertAddToFavouritesButtonDoesNotExist()
        assertRemoveFromFavouritesButtonExists()

        clickRemoveFromFavouritesButton()

        assertAddToFavouritesButtonExists()
        assertRemoveFromFavouritesButtonDoesNotExist()
    }

    private fun runTest(
        post: Post = PostCreator.post(
            title = PostTitle,
            body = PostBody,
        ),
        usePostForCache: Boolean = false,
        postResponse: FakeResponse = FakeResponse.CompletesNormally,
        readingTimeMinutes: Int = 1,
        testBlock: PostRobot.() -> Unit
    ) {
        // Setup test environment
        val postRepository = FakePostRepository(
            post = post,
            usePostForCache = usePostForCache,
            readingTimeMinutes = readingTimeMinutes,
        ).apply {
            this.postResponse = postResponse
        }

        composeRule.setContent {
            val viewModel = PostViewModel(
                dispatcher = Dispatchers.Main,
                postId = PostId,
                postRepository = postRepository,
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