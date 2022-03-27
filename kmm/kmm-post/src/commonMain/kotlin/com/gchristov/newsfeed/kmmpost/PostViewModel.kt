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

    //TODO: passing the repository into the viewmodel seems jumping ahead, it should
    // be always repository -> use case -> viewmodel?
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


                // This way should be enough as `decoratedPost` encapsulates the caching in the UseCase.
                // But we may want to ensure the cached values show up early,
                // so need to update the state with any cached value before new ones are loaded?

//                val newPost = decoratePostUseCase.decoratedPost(postId)
//                setState {
//                    copy(
//                        loading = false,
//                        post = newPost
//                    )
//                }

                // This way is similar to before, but exposing probably too much outside
                // of the UseCase (cachedPost, clearCache operations). This may be the only
                // way to do it, as we need to
                // update the state early on.
                // Still, it looks this should be encapsulated in UseCase
                // and expose only whatever needed to get the state updated desired behaviour
                decoratePostUseCase.apply {
                    cachedPost(postId)?.let { post ->
                        setState { copy(post = post) }
                        clearCache(postId)
                    }

                    // STRANGE:
                    // Adding this line here makes PostViewModelTest#onLoadSuccessSetsCache test pass
                    // due to the  assertTrue { viewModel.state.value.loading }
                    // delay(1000)
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