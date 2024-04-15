package com.gchristov.newsfeed.multiplatform.feed.data.usecase

import arrow.core.Either
import com.gchristov.newsfeed.multiplatform.feed.data.FeedRepository
import com.gchristov.newsfeed.multiplatform.feed.data.model.SectionedFeed

interface RedecorateSectionedFeedUseCase {
    suspend operator fun invoke(dto: Dto): Either<Throwable, SectionedFeed>

    data class Dto(val feed: SectionedFeed)
}

class RealRedecorateSectionedFeedUseCase(
    private val feedRepository: FeedRepository,
    private val flattenSectionedFeedUseCase: FlattenSectionedFeedUseCase,
    private val buildSectionedFeedUseCase: BuildSectionedFeedUseCase
) : RedecorateSectionedFeedUseCase {
    override suspend operator fun invoke(dto: RedecorateSectionedFeedUseCase.Dto) = with(dto) {
        val flattened = flattenSectionedFeedUseCase(feed)
        val redecorated = feedRepository.redecorateFeedPage(flattened)
        buildSectionedFeedUseCase(BuildSectionedFeedUseCase.Dto(redecorated))
    }
}