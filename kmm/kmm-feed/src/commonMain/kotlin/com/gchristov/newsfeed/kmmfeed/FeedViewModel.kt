package com.gchristov.newsfeed.kmmfeed

import com.gchristov.newsfeed.kmmcommonmvvm.CommonViewModel
import com.gchristov.newsfeed.kmmfeeddata.FeedRepository
import com.gchristov.newsfeed.kmmfeeddata.model.Feed
import com.gchristov.newsfeed.kmmfeeddata.model.decorate
import com.gchristov.newsfeed.kmmfeeddata.model.merge
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FeedViewModel(
    private val feedRepository: FeedRepository
) : CommonViewModel<FeedViewModel.State>(State()) {
    init {
        loadNextPage()
    }

    fun redecorateContent() {
        state.value.feed?.let { feed ->
            viewModelScope.launch {
                val redecoratedFeed = feed.posts.map { it.post.decorate(feedRepository) }
                withContext(Dispatchers.Main) {
                    setState {
                        copy(
                            feed = Feed(
                                posts = redecoratedFeed,
                                paging = feed.paging
                            ),
                        )
                    }
                }
            }
        }
    }

    fun refreshContent() {
        // Clear cache when user explicitly requests a refresh
        viewModelScope.launch { feedRepository.clearCache() }
        loadNextPage()
    }

    fun loadNextPage(startFromFirst: Boolean = true) {
        // The first page doesn't have a page key
        val nextPageKey = if (startFromFirst) null else state.value.feed?.paging?.next_cursor
        // Check if we have reached the end of the list
        if (!startFromFirst && nextPageKey.isNullOrEmpty()) {
            setState { copy(reachedEnd = true) }
            return
        }
        // Do not load more if we're already loading
        if (state.value.loadingMore) {
            return
        }
        setState {
            copy(
                loading = startFromFirst,
                loadingMore = !startFromFirst,
                reachedEnd = false,
                blockingError = null,
                nonBlockingError = null,
            )
        }
        viewModelScope.launch {
            try {
                val feed = feedRepository.feed(pageId = nextPageKey)
                withContext(Dispatchers.Main) {
                    setState {
                        copy(
                            loading = false,
                            loadingMore = false,
                            feed = if (startFromFirst) feed else state.value.feed?.merge(feed),
                        )
                    }
                }
            } catch (error: Exception) {
                error.printStackTrace()
                withContext(Dispatchers.Main) {
                    setState {
                        copy(
                            loading = false,
                            loadingMore = false,
                            blockingError = if (startFromFirst) error else null,
                            nonBlockingError = if (startFromFirst) null else error
                        )
                    }
                }
            }
        }
    }

    fun dismissNonBlockingError() {
        setState { copy(nonBlockingError = null) }
    }

    data class State(
        val loading: Boolean = false,
        val loadingMore: Boolean = false,
        val reachedEnd: Boolean = false,
        val blockingError: Throwable? = null,
        val nonBlockingError: Throwable? = null,
        val feed: Feed? = null
    )
}