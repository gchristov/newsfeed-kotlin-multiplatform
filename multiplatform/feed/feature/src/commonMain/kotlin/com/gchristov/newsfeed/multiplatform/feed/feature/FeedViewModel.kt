package com.gchristov.newsfeed.multiplatform.feed.feature

import arrow.core.raise.either
import com.gchristov.newsfeed.multiplatform.common.mvvm.CommonViewModel
import com.gchristov.newsfeed.multiplatform.feed.data.FeedRepository
import com.gchristov.newsfeed.multiplatform.feed.data.model.SectionedFeed
import com.gchristov.newsfeed.multiplatform.feed.data.model.hasNextPage
import com.gchristov.newsfeed.multiplatform.feed.data.usecase.BuildSectionedFeedUseCase
import com.gchristov.newsfeed.multiplatform.feed.data.usecase.MergeSectionedFeedUseCase
import com.gchristov.newsfeed.multiplatform.feed.data.usecase.RedecorateSectionedFeedUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNotNull

class FeedViewModel(
    dispatcher: CoroutineDispatcher,
    private val feedRepository: FeedRepository,
    private val redecorateSectionedFeedUseCase: RedecorateSectionedFeedUseCase,
    private val buildSectionedFeedUseCase: BuildSectionedFeedUseCase,
    private val mergeSectionedFeedUseCase: MergeSectionedFeedUseCase,
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
                either {
                    val redecorated = redecorateSectionedFeedUseCase(sectionedFeed).bind()
                    setState { copy(sectionedFeed = redecorated) }
                }.fold(
                    ifLeft = { it.printStackTrace() },
                    ifRight = { /* No-op */ }
                )
            }
        }
    }

    fun refreshContent() {
        // Clear cache when user explicitly requests a refresh
        launchUiCoroutine {
            either {
                feedRepository.clearCache().bind()
            }.fold(
                ifLeft = { it.printStackTrace() },
                ifRight = { /* No-op */ }
            )
        }
        loadNextPage()
    }

    fun loadNextPage(startFromFirst: Boolean = true) {
        val currentFeed = state.value.sectionedFeed
        val nextPage = if (startFromFirst) 1 else currentFeed?.currentPage!!.plus(1)
        val hasNextPage = if (startFromFirst) true else currentFeed?.hasNextPage()!!

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
            either {
                val searchQuery = feedRepository.searchQuery().bind()
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

                if (startFromFirst) {
                    feedRepository.cachedFeedPage().bind()?.let { decoratedFeed ->
                        val cachedSectionedFeed = buildSectionedFeedUseCase(decoratedFeed).bind()
                        setState { copy(sectionedFeed = cachedSectionedFeed) }
                    }
                }

                val flatNewFeed = feedRepository.feedPage(
                    pageId = nextPage,
                    feedQuery = searchQuery,
                ).bind()
                val sectionedNewFeed = buildSectionedFeedUseCase(flatNewFeed).bind()

                val result = if (currentFeed != null && !startFromFirst) {
                    mergeSectionedFeedUseCase(
                        thisFeed = currentFeed,
                        newFeed = sectionedNewFeed
                    ).bind()
                } else sectionedNewFeed

                setState {
                    copy(
                        loading = false,
                        loadingMore = false,
                        sectionedFeed = result,
                    )
                }
            }.fold(
                ifLeft = { error ->
                    error.printStackTrace()
                    val blocking = if (state.value.sectionedFeed == null) error else null
                    val nonBlocking = if (blocking == null) error else null
                    setState {
                        copy(
                            loading = false,
                            loadingMore = false,
                            blockingError = blocking,
                            nonBlockingError = nonBlocking,
                        )
                    }
                },
                ifRight = { /* No-op */ },
            )
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
        val searchQuery: String? = null,
        val searchWidgetState: SearchWidgetState = SearchWidgetState.CLOSED,
    )
}

private const val DEBOUNCE_INTERVAL_MS = 500L