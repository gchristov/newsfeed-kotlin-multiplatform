package com.gchristov.newsfeed.multiplatform.feed.data.usecase

import com.gchristov.newsfeed.multiplatform.feed.data.FeedPage
import com.gchristov.newsfeed.multiplatform.feed.data.model.DecoratedFeedPage
import com.gchristov.newsfeed.multiplatform.feed.data.model.SectionedFeed
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

interface FlattenSectionedFeedUseCase {
    suspend operator fun invoke(feed: SectionedFeed): DecoratedFeedPage
}

class RealFlattenSectionedFeedUseCase(
    private val dispatcher: CoroutineDispatcher,
) : FlattenSectionedFeedUseCase {
    override suspend operator fun invoke(feed: SectionedFeed): DecoratedFeedPage =
        withContext(dispatcher) {
            val items = feed.sections.flatMap { it.feedItems }
            val page = FeedPage(
                pageId = feed.currentPage.toLong(),
                pages = feed.pages.toLong(),
            )
            DecoratedFeedPage(
                raw = page,
                items = items,
            )
        }
}