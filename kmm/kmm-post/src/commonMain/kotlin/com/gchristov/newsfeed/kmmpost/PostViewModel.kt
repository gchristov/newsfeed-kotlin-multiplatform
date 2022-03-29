package com.gchristov.newsfeed.kmmpost

import com.gchristov.newsfeed.kmmcommonmvvm.CommonViewModel
import com.gchristov.newsfeed.kmmpostdata.PostRepository
import com.gchristov.newsfeed.kmmpostdata.model.DecoratedPost
import com.gchristov.newsfeed.kmmpostdata.usecase.DecoratePostUseCase
import com.gchristov.newsfeed.kmmpostdata.usecase.ReadingTimeCalculationUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay

class PostViewModel(
    dispatcher: CoroutineDispatcher,
    private var postId: String,
    private val postRepository: PostRepository,
    private val decoratePostUseCase: DecoratePostUseCase
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

                // This may seem like too many exposed operations,
                // but the reasoning behind it is we want to show
                // the cached post while loading new one/s
                decoratePostUseCase.apply {

                    // always show cached post
                    cachedPost(postId)?.let { post ->
                        setState { copy(post = post) }
                        clearCache(postId)
                    }

                    val newPost = decoratedPost(postId)
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

                decoratePostUseCase.apply {
                    postRepository.toggleFavourite(postId)
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