package com.gchristov.newsfeed.multiplatform.feed.data.usecase

import com.gchristov.newsfeed.multiplatform.feed.data.FeedPage
import com.gchristov.newsfeed.multiplatform.feed.data.model.DecoratedFeedPage
import com.gchristov.newsfeed.multiplatform.feed.data.model.SectionedFeed
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