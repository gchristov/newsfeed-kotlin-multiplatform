package com.gchristov.newsfeed.multiplatform.post.feature

import arrow.core.raise.either
import com.gchristov.newsfeed.multiplatform.common.mvvm.CommonViewModel
import com.gchristov.newsfeed.multiplatform.post.data.PostRepository
import com.gchristov.newsfeed.multiplatform.post.data.model.DecoratedPost
import kotlinx.coroutines.CoroutineDispatcher

class PostViewModel(
    dispatcher: CoroutineDispatcher,
    private var postId: String,
    private val postRepository: PostRepository,
) : CommonViewModel<PostViewModel.State>(
    dispatcher = dispatcher,
    initialState = State(),
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
            either {
                postRepository.cachedPost(postId).bind()?.let { decoratedPost ->
                    setState { copy(post = decoratedPost) }
                }

                val newPost = postRepository.post(postId).bind()

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
        state.value.post?.let { post ->
            launchUiCoroutine {
                either {
                    postRepository.toggleFavourite(post.raw.id).bind()
                    val updatedPost = postRepository.cachedPost(post.raw.id).bind()
                    setState { copy(post = updatedPost) }
                }.fold(
                    ifLeft = { it.printStackTrace() },
                    ifRight = { /* No-op */ }
                )
            }
        }
    }

    data class State(
        val loading: Boolean = false,
        val blockingError: Throwable? = null,
        val post: DecoratedPost? = null,
    )
}