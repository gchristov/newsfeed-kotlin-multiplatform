package com.gchristov.newsfeed.post

import com.gchristov.newsfeed.commoncomposetest.CommonComposeTestClass
import com.gchristov.newsfeed.kmmcommontest.FakeCoroutineDispatcher
import com.gchristov.newsfeed.kmmcommontest.FakeResponse
import com.gchristov.newsfeed.kmmpost.PostViewModel
import com.gchristov.newsfeed.kmmpostdata.model.DecoratedPost
import com.gchristov.newsfeed.kmmpostdata.usecase.DecoratePostUseCase
import com.gchristov.newsfeed.kmmposttestfixtures.FakePostRepository
import com.gchristov.newsfeed.kmmposttestfixtures.PostCreator
import com.gchristov.newsfeed.posttestfixtures.PostRobot
import com.gchristov.newsfeed.posttestfixtures.post
import kotlinx.coroutines.CoroutineDispatcher
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

        val decoratePostUseCase = DecoratePostUseCase(
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

// 549 words ~= 3 minutes
private const val LargeReadingPostTime = "3 min read"
private const val LargePostBody = """
    Lorem ipsum dolor sit amet. Id illum magnam ut quis maxime ut molestiae fuga qui voluptatibus quod ipsam voluptatem qui voluptatem autem sit voluptates labore. Eos minus officia rem earum facilis qui nobis eligendi! Ab molestiae dolorum et dolor internos sed veniam soluta est eius corporis! Non excepturi perferendis aut voluptas iure eum Quis fugit sed corporis possimus et ipsum ducimus.

Nam dolorem eveniet sed voluptate unde eum consequatur accusantium est doloribus laboriosam. Qui voluptas explicabo sed voluptatem molestias sed sunt earum est corrupti voluptatem vel minima necessitatibus. Aut ipsa pariatur ut culpa ullam sed error consequuntur aut omnis voluptatum est pariatur ipsam.

Est tempora quod quos reprehenderit et maiores galisum in molestiae itaque qui facere fuga et modi praesentium. Et libero illum a corporis amet sed consequuntur suscipit? Et quia possimus ut exercitationem quia aut dolorem deleniti et rerum rerum.

Qui distinctio omnis qui labore molestiae 33 similique excepturi eos illum Quis vel debitis voluptates id voluptas consequatur. Aut facilis veritatis qui quia aliquid eum ipsum mollitia non vitae nihil ea nobis quisquam cum aliquam quam ea excepturi iusto.

Id neque voluptatibus 33 ipsam ducimus qui dolorem odit et beatae voluptas qui quidem voluptatem nam voluptatem sequi! Ut dolor ipsam sed dolorem modi est fugiat fuga ad totam numquam qui cupiditate deleniti aut maxime obcaecati. Eum facilis illum vel iure adipisci sed debitis vero ab assumenda delectus quo tempore perferendis quo nihil odio ut officiis temporibus. Ut explicabo fugiat non repellendus asperiores et tempore ratione non natus galisum.

Eum rerum cupiditate est pariatur odio et consequatur laudantium et sint animi sed delectus explicabo a impedit placeat. Eos veniam fugit eos dicta debitis At natus provident. Quo temporibus unde ut esse molestiae qui perspiciatis dolorem ut consequatur officiis cumque architecto.

Ea nulla expedita ea eaque dolorem et earum sapiente qui quam quia est sapiente nostrum aut ducimus excepturi ut soluta ullam. Est aspernatur nihil qui repellendus ullam in dolores animi sed autem quas et minima dolor et velit adipisci. Et exercitationem nisi vel facilis optio sed odit cumque vel explicabo similique et maxime omnis ut commodi quas ea suscipit praesentium. Et natus placeat rerum repudiandae ab quasi magnam.

Eum explicabo voluptate et eveniet Quis in maxime neque est porro dolorem sit nihil quam est iusto galisum id itaque sint. Ex ducimus inventore aut minus dolores ut voluptas vero. Aut reiciendis corrupti et totam voluptas ea repellat architecto et veniam soluta sed explicabo nostrum eos veniam recusandae et neque Quis. Et sint explicabo id quae amet aut modi inventore aut quas quos ut quaerat itaque et doloremque nihil.

Et rerum deserunt et autem nesciunt et sint enim cum reiciendis delectus rem repudiandae excepturi est labore cupiditate est totam dolor. Est maxime sunt et adipisci doloremque aut soluta maxime eos doloribus iste. Est nihil quae est neque tenetur et saepe recusandae aut eius voluptates. Et ipsam dolores quo quia porro nam iure maiores ut omnis veritatis in molestiae totam.

Est mollitia repellendus et quia sequi et eveniet nulla consequatur repellendus hic quia nostrum! Ab asperiores dolor qui corrupti necessitatibus vel quos necessitatibus aut iure rerum et distinctio velit. Quo modi voluptate cum incidunt quae quo amet omnis ut consequuntur libero aut nostrum dolores est voluptatum aliquid a iure minima. Ea maiores voluptas vel voluptas fuga eos quae asperiores est fugit totam?

"""
