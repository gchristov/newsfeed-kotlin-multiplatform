package com.gchristov.newsfeed.multiplatform.feed.data.usecase

import arrow.core.Either
import arrow.core.flatMap
import com.gchristov.newsfeed.multiplatform.feed.data.FeedRepository
import com.gchristov.newsfeed.multiplatform.feed.data.model.SectionedFeed

interface GetSectionedFeedPageUseCase {
    suspend operator fun invoke(dto: Dto): Either<Throwable, SectionedFeed>

    data class Dto(
        val pageId: Int = 1,
        val feedQuery: String,
        val currentFeed: SectionedFeed? = null,
    )
}

class RealGetSectionedFeedPageUseCase(
    private val feedRepository: FeedRepository,
    private val buildSectionedFeedUseCase: BuildSectionedFeedUseCase,
    private val mergeSectionedFeedUseCase: MergeSectionedFeedUseCase,
) : GetSectionedFeedPageUseCase {
    override suspend operator fun invoke(
        dto: GetSectionedFeedPageUseCase.Dto
    ) = with(dto) {
        val flatNewFeed = feedRepository.feedPage(
            pageId = pageId,
            feedQuery = feedQuery
        )
        buildSectionedFeedUseCase(BuildSectionedFeedUseCase.Dto(flatNewFeed))
            .flatMap { newFeed ->
                currentFeed?.let {
                    mergeSectionedFeedUseCase(
                        MergeSectionedFeedUseCase.Dto(
                            currentFeed = currentFeed,
                            newFeed = newFeed,
                        )
                    )
                } ?: Either.Right(newFeed)
            }
    }
}