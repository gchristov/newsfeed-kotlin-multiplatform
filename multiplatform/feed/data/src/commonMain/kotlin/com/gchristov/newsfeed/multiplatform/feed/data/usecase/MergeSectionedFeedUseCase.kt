package com.gchristov.newsfeed.multiplatform.feed.data.usecase

import com.gchristov.newsfeed.multiplatform.feed.data.model.DecoratedFeedItem
import com.gchristov.newsfeed.multiplatform.feed.data.model.SectionedFeed
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

interface MergeSectionedFeedUseCase {
    suspend operator fun invoke(
        thisFeed: SectionedFeed,
        newFeed: SectionedFeed,
    ): SectionedFeed
}

class RealMergeSectionedFeedUseCase(
    private val dispatcher: CoroutineDispatcher
) : MergeSectionedFeedUseCase {
    override suspend operator fun invoke(
        thisFeed: SectionedFeed,
        newFeed: SectionedFeed,
    ): SectionedFeed =
        withContext(dispatcher) {
            SectionedFeed(
                pages = newFeed.pages,
                currentPage = newFeed.currentPage,
                sections = mutableListOf<SectionedFeed.Section>().apply {
                    if (thisFeed.sections.isEmpty()) {
                        // No existing sections so need to merge - just add new sections
                        addAll(newFeed.sections)
                    } else if (newFeed.sections.isEmpty()) {
                        // No new sections so need to merge - just add existing sections
                        addAll(thisFeed.sections)
                    } else {
                        // Add all existing sections
                        addAll(thisFeed.sections)
                        // Merge the last and first sections together if they are from the same type, otherwise
                        // append the new sections
                        val lastOldSection = thisFeed.sections.last()
                        val firstNewSection = newFeed.sections.first()
                        if (lastOldSection.type == firstNewSection.type) {
                            val mergedLastOldSection = SectionedFeed.Section(
                                type = lastOldSection.type,
                                feedItems = mutableListOf<DecoratedFeedItem>().apply {
                                    addAll(lastOldSection.feedItems)
                                    addAll(firstNewSection.feedItems)
                                }
                            )
                            set(thisFeed.sections.lastIndex, mergedLastOldSection)
                            addAll(newFeed.sections.subList(1, newFeed.sections.size))
                        } else {
                            addAll(newFeed.sections)
                        }
                    }
                }
            )
        }
}