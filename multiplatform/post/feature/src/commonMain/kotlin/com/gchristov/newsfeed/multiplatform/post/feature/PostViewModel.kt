package com.gchristov.newsfeed.multiplatform.post.feature

import arrow.core.raise.either
import com.gchristov.newsfeed.multiplatform.common.mvvm.CommonViewModel
import com.gchristov.newsfeed.multiplatform.post.data.PostRepository
import com.gchristov.newsfeed.multiplatform.post.data.model.DecoratedPost
import kotlinx.coroutines.CoroutineDispatcher

class PostViewModel(
    dispatcher: CoroutineDispatcher,
    postId: String,
    private val postRepository: PostRepository,
) : CommonViewModel<PostViewModel.State>(
    dispatcher = dispatcher,
    initialState = State(postId = postId),
) {
    init {
        loadContent()
    }

    fun resetPostId(postId: String) {
        setState {
            copy(
                postId = postId,
                post = null,
            )
        }
        loadContent()
    }

    fun loadContent() {
        setState {
            copy(
                loading = true,
                blockingError = null,
            )
        }
        launchCoroutine {
            either {
                postRepository.cachedPost(state.value.postId).bind()?.let { decoratedPost ->
                    setState { copy(post = decoratedPost) }
                }

                val newPost = postRepository.post(state.value.postId).bind()

                setState {
                    copy(
                        loading = false,
                        post = newPost,
                    )
                }
            }.fold(
                ifLeft = { error ->
                    error.printStackTrace()
                    setState {
                        copy(
                            loading = false,
                            blockingError = error,
                        )
                    }
                },
                ifRight = { /* No-op */ },
            )
        }
    }

    fun onToggleFavourite() {
        launchCoroutine {
            either {
                postRepository.toggleFavourite(state.value.postId).bind()
                loadContent()
            }.fold(
                ifLeft = { it.printStackTrace() },
                ifRight = { /* No-op */ }
            )
        }
    }

    data class State(
        val loading: Boolean = false,
        val blockingError: Throwable? = null,
        val postId: String,
        val post: DecoratedPost? = null,
    )
}