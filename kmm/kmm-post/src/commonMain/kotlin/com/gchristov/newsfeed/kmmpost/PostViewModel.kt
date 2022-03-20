package com.gchristov.newsfeed.kmmpost

import com.gchristov.newsfeed.kmmcommonmvvm.CommonViewModel
import com.gchristov.newsfeed.kmmpostdata.PostRepository
import com.gchristov.newsfeed.kmmpostdata.model.DecoratedPost
import com.gchristov.newsfeed.kmmpostdata.usecase.ReadingTimeCalculationUseCase
import kotlinx.coroutines.CoroutineDispatcher

class PostViewModel(
    dispatcher: CoroutineDispatcher,
    private var postId: String,
    private val postRepository: PostRepository,
    private val readingTimeCalculationUseCase: ReadingTimeCalculationUseCase
) : CommonViewModel<PostViewModel.State>(
    dispatcher = dispatcher,
    initialState = State()
) {
    init {
        loadContent()
    }

    fun resetPostId(postId: String) {
        this.postId = postId
        loadContent()
    }

    fun loadContent() {
        setState {
            copy(
                loading = true,
                blockingError = null
            )
        }
        launchUiCoroutine {
            try {
                postRepository.apply {
                    // Always show cached post
                    cachedPost(postId)?.let { post ->

                        // Option 2: using a useCase for readingTimeCalculation

                        // Cannot directly use the useCase here directly
                        // to calculate reading time:
                        // - all is immutable
                        // - it cannot take advantage of cache

                        // Looks like it needs to stay in the repository or
                        // move the whole redecoration to an useCase and delegate
                        // few things there instead of using the postRepository directly
                        // there

                        // post.readingTimeMinutes = calculateReadingTimeMinutes(post.raw)
                        // readingTimeCalculationUseCase(post)
                        setState { copy(post = post) }
                        clearCache(postId)
                    }
                    val newPost = postRepository.post(postId)
                    setState {
                        copy(
                            loading = false,
                            post = newPost
                        )
                    }
                }
            } catch (error: Exception) {
                error.printStackTrace()
                setState {
                    copy(
                        loading = false,
                        blockingError = error
                    )
                }
            }
        }
    }

    fun onToggleFavourite() {
        state.value.post?.let { post ->
            launchUiCoroutine {
                postRepository.apply {
                    toggleFavourite(postId)
                    val redecoratedPost = redecoratePost(post)
                    setState { copy(post = redecoratedPost) }
                }
            }
        }
    }

    data class State(
        val loading: Boolean = false,
        val blockingError: Throwable? = null,
        val post: DecoratedPost? = null,
    )
}