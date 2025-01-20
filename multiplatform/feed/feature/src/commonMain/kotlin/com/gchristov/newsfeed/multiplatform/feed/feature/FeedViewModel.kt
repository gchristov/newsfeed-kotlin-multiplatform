package com.gchristov.newsfeed.multiplatform.feed.feature

import com.gchristov.newsfeed.multiplatform.common.mvvm.CommonViewModel
import com.gchristov.newsfeed.multiplatform.feed.data.FeedRepository
import com.gchristov.newsfeed.multiplatform.feed.data.model.SectionedFeed
import com.gchristov.newsfeed.multiplatform.feed.data.model.hasNextPage
import com.gchristov.newsfeed.multiplatform.feed.data.usecase.GetSectionedFeedUseCase
import com.gchristov.newsfeed.multiplatform.feed.data.usecase.RedecorateSectionedFeedUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNotNull

class FeedViewModel(
    dispatcher: CoroutineDispatcher,
    private val feedRepository: FeedRepository,
    private val getSectionedFeedUseCase: GetSectionedFeedUseCase,
    private val redecorateSectionedFeedUseCase: RedecorateSectionedFeedUseCase,
) : CommonViewModel<FeedViewModel.State>(
    dispatcher = dispatcher,
    initialState = State()
) {
    private val searchQueryFlow: MutableStateFlow<String?> = MutableStateFlow(null)

    init {
        observeSearchQuery()
        loadNextPage()
    }

    /**
     * Observer function placed on the search text being updated from the UI.
     *
     * The MutableStateFlow, representing the sequence of keystrokes being emitted
     * by the user, is debounced every 500ms so that downstream services (API,
     * repository...) are not hit an unnecessary number of times and only when relevant
     * results can be returned
     */
    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {
        launchUiCoroutine {
            searchQueryFlow
                .debounce(DEBOUNCE_INTERVAL_MS)
                .filterNotNull()
                .collect { debouncedText ->
                    feedRepository.saveSearchQuery(debouncedText)
                    loadNextPage()
                }
        }
    }

    fun onSearchTextChanged(newQuery: String) {
        setState { copy(searchQuery = newQuery) }
        searchQueryFlow.value = newQuery
    }

    fun onSearchStateChanged(newSearchState: SearchWidgetState) {
        setState { copy(searchWidgetState = newSearchState) }
    }

    fun redecorateContent() {
        state.value.sectionedFeed?.let { sectionedFeed ->
            launchUiCoroutine {
                val redecorated = redecorateSectionedFeedUseCase(sectionedFeed)
                setState { copy(sectionedFeed = redecorated) }
            }
        }
    }

    fun refreshContent() {
        // Clear cache when user explicitly requests a refresh
        launchUiCoroutine { feedRepository.clearCache() }
        loadNextPage()
    }

    fun loadNextPage(startFromFirst: Boolean = true) {
        val nextPage = if (startFromFirst) 1 else state.value.sectionedFeed?.currentPage!!.plus(1)
        val hasNextPage = if (startFromFirst) true else state.value.sectionedFeed?.hasNextPage()!!
        // Check if we have reached the end of the list
        if (!hasNextPage) {
            setState { copy(reachedEnd = true) }
            return
        }
        // Do not load more if we're already loading
        if (state.value.loadingMore) {
            return
        }
        launchUiCoroutine {
            try {
                val searchQuery = feedRepository.searchQuery() ?: DEFAULT_SEARCH_QUERY
                setState {
                    copy(
                        loading = startFromFirst,
                        loadingMore = !startFromFirst,
                        reachedEnd = false,
                        blockingError = null,
                        nonBlockingError = null,
                        searchQuery = searchQuery,
                    )
                }

                val feedUpdate = getSectionedFeedUseCase(
                    pageId = nextPage,
                    feedQuery = searchQuery,
                    currentFeed = state.value.sectionedFeed,
                    // Only request cache if we're starting from the first page
                    onCache = if (startFromFirst) { cache ->
                        setState { copy(sectionedFeed = cache) }
                        launchUiCoroutine { feedRepository.clearCache() }
                    } else null
                )
                setState {
                    copy(
                        loading = false,
                        loadingMore = false,
                        sectionedFeed = feedUpdate,
                    )
                }
            } catch (error: Exception) {
                error.printStackTrace()
                val blocking = if (state.value.sectionedFeed == null) error else null
                val nonBlocking = if (blocking == null) error else null
                setState {
                    copy(
                        loading = false,
                        loadingMore = false,
                        blockingError = blocking,
                        nonBlockingError = nonBlocking
                    )
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
        val sectionedFeed: SectionedFeed? = null,
        val searchQuery: String = DEFAULT_SEARCH_QUERY,
        val searchWidgetState: SearchWidgetState = SearchWidgetState.CLOSED,
    )
}

private const val DEBOUNCE_INTERVAL_MS = 500L
private const val DEFAULT_SEARCH_QUERY = "brexit,fintech"