package com.gchristov.newsfeed.kmmpost

import com.gchristov.newsfeed.kmmcommonmvvm.CommonViewModel
import com.gchristov.newsfeed.kmmfeeddata.FeedRepository
import com.gchristov.newsfeed.kmmfeeddata.model.DecoratedPost
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PostViewModel(
    private var postId: String,
    private val feedRepository: FeedRepository,
) : CommonViewModel<PostViewModel.State>(State()) {
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
        viewModelScope.launch {
            try {
                val post = feedRepository.post(postId)
                withContext(Dispatchers.Main) {
                    setState {
                        copy(
                            loading = false,
                            post = post
                        )
                    }
                }
            } catch (error: Exception) {
                error.printStackTrace()
                withContext(Dispatchers.Main) {
                    setState {
                        copy(
                            loading = false,
                            blockingError = error
                        )
                    }
                }
            }
        }
    }

    fun onToggleFavourite() {
        state.value.post?.let { post ->
            setState {
                copy(
                    post = DecoratedPost(
                        post.post,
                        feedRepository.toggleFavourite(postId)
                    )
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