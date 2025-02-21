package com.gchristov.newsfeed.multiplatform.feed.data.usecase

import arrow.core.Either
import com.gchristov.newsfeed.multiplatform.feed.data.FeedPage
import com.gchristov.newsfeed.multiplatform.feed.data.model.DecoratedFeedPage
import com.gchristov.newsfeed.multiplatform.feed.data.model.SectionedFeed
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

interface FlattenSectionedFeedUseCase {
    suspend operator fun invoke(dto: Dto): Either<Throwable, DecoratedFeedPage>

    data class Dto(val feed: SectionedFeed)
}

class RealFlattenSectionedFeedUseCase(
    private val dispatcher: CoroutineDispatcher,
) : FlattenSectionedFeedUseCase {
    override suspend operator fun invoke(
        dto: FlattenSectionedFeedUseCase.Dto,
    ): Either<Throwable, DecoratedFeedPage> = withContext(dispatcher) {
        val items = dto.feed.sections.flatMap { it.feedItems }
        val page = FeedPage(
            pageId = dto.feed.currentPage.toLong(),
            pages = dto.feed.pages.toLong(),
        )
        Either.Right(
            DecoratedFeedPage(
                raw = page,
                items = items,
            )
        )
    }
}