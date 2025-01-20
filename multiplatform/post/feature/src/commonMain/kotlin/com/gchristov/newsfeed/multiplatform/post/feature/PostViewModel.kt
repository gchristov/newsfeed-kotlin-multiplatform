package com.gchristov.newsfeed.multiplatform.post.feature

import com.gchristov.newsfeed.multiplatform.common.mvvm.CommonViewModel
import com.gchristov.newsfeed.multiplatform.post.data.PostRepository
import com.gchristov.newsfeed.multiplatform.post.data.model.DecoratedPost
import com.gchristov.newsfeed.multiplatform.post.data.usecase.DecoratePostUseCase
import kotlinx.coroutines.CoroutineDispatcher

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

                val newPost = decoratePostUseCase(
                    postId = postId,
                    onCache = { cachedPost ->
                        setState { copy(post = cachedPost) }
                    }
                )

                setState {
                    copy(
                        loading = false,
                        post = newPost
                    )
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
                postRepository.toggleFavourite(post.raw.id)
                val newPost = decoratePostUseCase(post.raw.id)
                setState { copy(post = newPost) }
            }
        }
    }

    data class State(
        val loading: Boolean = false,
        val blockingError: Throwable? = null,
        val post: DecoratedPost? = null,
    )
}