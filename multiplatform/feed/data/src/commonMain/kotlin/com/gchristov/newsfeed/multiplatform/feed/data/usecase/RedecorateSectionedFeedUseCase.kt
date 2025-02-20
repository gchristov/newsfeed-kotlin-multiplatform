package com.gchristov.newsfeed.multiplatform.feed.data.usecase

import arrow.core.Either
import arrow.core.raise.either
import com.gchristov.newsfeed.multiplatform.feed.data.FeedRepository
import com.gchristov.newsfeed.multiplatform.feed.data.model.DecoratedFeedPage
import com.gchristov.newsfeed.multiplatform.feed.data.model.SectionedFeed
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

interface RedecorateSectionedFeedUseCase {
    suspend operator fun invoke(feed: SectionedFeed): Either<Throwable, SectionedFeed>
}

class RealRedecorateSectionedFeedUseCase(
    private val dispatcher: CoroutineDispatcher,
    private val feedRepository: FeedRepository,
    private val flattenSectionedFeedUseCase: FlattenSectionedFeedUseCase,
    private val buildSectionedFeedUseCase: BuildSectionedFeedUseCase,
) : RedecorateSectionedFeedUseCase {
    override suspend operator fun invoke(
        feed: SectionedFeed,
    ): Either<Throwable, SectionedFeed> = withContext(dispatcher) {
        either {
            val flattened = flattenSectionedFeedUseCase(feed).bind()
            val redecorated = feedRepository.redecorateFeedPage(flattened).bind()
            buildSectionedFeedUseCase(redecorated).bind()
        }
    }
}