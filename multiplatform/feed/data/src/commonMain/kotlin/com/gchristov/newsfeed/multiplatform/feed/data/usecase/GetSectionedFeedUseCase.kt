package com.gchristov.newsfeed.multiplatform.feed.data.usecase

import com.gchristov.newsfeed.multiplatform.feed.data.FeedRepository
import com.gchristov.newsfeed.multiplatform.feed.data.model.SectionedFeed

class GetSectionedFeedUseCase(
    private val feedRepository: FeedRepository,
    private val buildSectionedFeedUseCase: BuildSectionedFeedUseCase,
    private val mergeSectionedFeedUseCase: MergeSectionedFeedUseCase
) {
    suspend operator fun invoke(
        pageId: Int = 1,
        feedQuery: String,
        currentFeed: SectionedFeed? = null,
        onCache: ((SectionedFeed) -> Unit)? = null
    ): SectionedFeed {
        onCache?.let {
            feedRepository.cachedFeedPage()?.let {
                val cache = buildSectionedFeedUseCase(it)
                onCache(cache)
            }
        }
        val flatNewFeed = feedRepository.feedPage(
            pageId = pageId,
            feedQuery = feedQuery
        )
        val sectionedNewFeed = buildSectionedFeedUseCase(flatNewFeed)
        if (currentFeed != null && onCache == null) {
            return mergeSectionedFeedUseCase(
                thisFeed = currentFeed,
                newFeed = sectionedNewFeed
            )
        }
        return sectionedNewFeed
    }
}