package com.gchristov.newsfeed.kmmfeeddata.usecase

import com.gchristov.newsfeed.kmmfeeddata.FeedPage
import com.gchristov.newsfeed.kmmfeeddata.model.DecoratedFeedPage
import com.gchristov.newsfeed.kmmfeeddata.model.SectionedFeed
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class FlattenSectionedFeedUseCase(private val dispatcher: CoroutineDispatcher) {
    suspend operator fun invoke(feed: SectionedFeed): DecoratedFeedPage =
        withContext(dispatcher) {
            val items = feed.sections.flatMap { it.feedItems }
            val page = FeedPage(
                pageId = feed.currentPage,
                pages = feed.pages
            )
            DecoratedFeedPage(
                raw = page,
                items = items
            )
        }
}