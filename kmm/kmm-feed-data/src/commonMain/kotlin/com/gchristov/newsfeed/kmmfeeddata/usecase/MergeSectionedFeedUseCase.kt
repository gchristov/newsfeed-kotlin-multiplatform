package com.gchristov.newsfeed.kmmfeeddata.usecase

import com.gchristov.newsfeed.kmmfeeddata.model.DecoratedFeedItem
import com.gchristov.newsfeed.kmmfeeddata.model.SectionedFeed
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class MergeSectionedFeedUseCase(private val dispatcher: CoroutineDispatcher) {
    suspend operator fun invoke(
        thisFeed: SectionedFeed,
        newFeed: SectionedFeed
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