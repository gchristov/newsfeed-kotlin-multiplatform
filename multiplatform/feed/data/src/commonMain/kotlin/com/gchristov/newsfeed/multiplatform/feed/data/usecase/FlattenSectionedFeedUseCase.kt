package com.gchristov.newsfeed.multiplatform.feed.data.usecase

import arrow.core.Either
import com.gchristov.newsfeed.multiplatform.feed.data.FeedPage
import com.gchristov.newsfeed.multiplatform.feed.data.model.DecoratedFeedPage
import com.gchristov.newsfeed.multiplatform.feed.data.model.SectionedFeed
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

interface FlattenSectionedFeedUseCase {
    suspend operator fun invoke(feed: SectionedFeed): Either<Throwable, DecoratedFeedPage>
}

class RealFlattenSectionedFeedUseCase(
    private val dispatcher: CoroutineDispatcher,
) : FlattenSectionedFeedUseCase {
    override suspend operator fun invoke(
        feed: SectionedFeed,
    ): Either<Throwable, DecoratedFeedPage> = withContext(dispatcher) {
        val items = feed.sections.flatMap { it.feedItems }
        val page = FeedPage(
            pageId = feed.currentPage.toLong(),
            pages = feed.pages.toLong(),
        )
        Either.Right(
            DecoratedFeedPage(
                raw = page,
                items = items,
            )
        )
    }
}