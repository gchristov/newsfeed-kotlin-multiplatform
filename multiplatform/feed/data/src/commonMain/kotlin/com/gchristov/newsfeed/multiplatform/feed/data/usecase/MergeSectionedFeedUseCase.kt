package com.gchristov.newsfeed.multiplatform.feed.data.usecase

import arrow.core.Either
import com.gchristov.newsfeed.multiplatform.feed.data.model.DecoratedFeedItem
import com.gchristov.newsfeed.multiplatform.feed.data.model.SectionedFeed
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

interface MergeSectionedFeedUseCase {
    suspend operator fun invoke(dto: Dto): Either<Throwable, SectionedFeed>

    data class Dto(
        val thisFeed: SectionedFeed,
        val newFeed: SectionedFeed,
    )
}

class RealMergeSectionedFeedUseCase(
    private val dispatcher: CoroutineDispatcher
) : MergeSectionedFeedUseCase {
    override suspend operator fun invoke(
        dto: MergeSectionedFeedUseCase.Dto
    ): Either<Throwable, SectionedFeed> = withContext(dispatcher) {
        val feed = SectionedFeed(
            pages = dto.newFeed.pages,
            currentPage = dto.newFeed.currentPage,
            sections = mutableListOf<SectionedFeed.Section>().apply {
                if (dto.thisFeed.sections.isEmpty()) {
                    // No existing sections so need to merge - just add new sections
                    addAll(dto.newFeed.sections)
                } else if (dto.newFeed.sections.isEmpty()) {
                    // No new sections so need to merge - just add existing sections
                    addAll(dto.thisFeed.sections)
                } else {
                    // Add all existing sections
                    addAll(dto.thisFeed.sections)
                    // Merge the last and first sections together if they are from the same type, otherwise
                    // append the new sections
                    val lastOldSection = dto.thisFeed.sections.last()
                    val firstNewSection = dto.newFeed.sections.first()
                    if (lastOldSection.type == firstNewSection.type) {
                        val mergedLastOldSection = SectionedFeed.Section(
                            type = lastOldSection.type,
                            feedItems = mutableListOf<DecoratedFeedItem>().apply {
                                addAll(lastOldSection.feedItems)
                                addAll(firstNewSection.feedItems)
                            }
                        )
                        set(dto.thisFeed.sections.lastIndex, mergedLastOldSection)
                        addAll(dto.newFeed.sections.subList(1, dto.newFeed.sections.size))
                    } else {
                        addAll(dto.newFeed.sections)
                    }
                }
            }
        )
        Either.Right(feed)
    }
}