package com.gchristov.newsfeed.multiplatform.feed.data.usecase

import arrow.core.Either
import com.gchristov.newsfeed.multiplatform.feed.data.model.DecoratedFeedItem
import com.gchristov.newsfeed.multiplatform.feed.data.model.SectionedFeed
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

interface MergeSectionedFeedUseCase {
    suspend operator fun invoke(dto: Dto): Either<Throwable, SectionedFeed>

    data class Dto(
        val currentFeed: SectionedFeed,
        val newFeed: SectionedFeed,
    )
}

class RealMergeSectionedFeedUseCase(
    private val dispatcher: CoroutineDispatcher
) : MergeSectionedFeedUseCase {
    override suspend operator fun invoke(
        dto: MergeSectionedFeedUseCase.Dto
    ) = withContext(dispatcher) {
        with(dto) {
            val sections = mutableListOf<SectionedFeed.Section>().apply {
                if (currentFeed.sections.isEmpty()) {
                    // No existing sections so need to merge - just add new sections
                    addAll(newFeed.sections)
                } else if (newFeed.sections.isEmpty()) {
                    // No new sections so need to merge - just add existing sections
                    addAll(currentFeed.sections)
                } else {
                    // Add all existing sections
                    addAll(currentFeed.sections)
                    // Merge the last and first sections together if they are from the same type, otherwise
                    // append the new sections
                    val lastOldSection = currentFeed.sections.last()
                    val firstNewSection = newFeed.sections.first()
                    if (lastOldSection.type == firstNewSection.type) {
                        val mergedLastOldSection = SectionedFeed.Section(
                            type = lastOldSection.type,
                            feedItems = mutableListOf<DecoratedFeedItem>().apply {
                                addAll(lastOldSection.feedItems)
                                addAll(firstNewSection.feedItems)
                            }
                        )
                        set(currentFeed.sections.lastIndex, mergedLastOldSection)
                        addAll(newFeed.sections.subList(1, newFeed.sections.size))
                    } else {
                        addAll(newFeed.sections)
                    }
                }
            }
            Either.Right(
                SectionedFeed(
                    pages = newFeed.pages,
                    currentPage = newFeed.currentPage,
                    sections = sections
                )
            )
        }
    }
}