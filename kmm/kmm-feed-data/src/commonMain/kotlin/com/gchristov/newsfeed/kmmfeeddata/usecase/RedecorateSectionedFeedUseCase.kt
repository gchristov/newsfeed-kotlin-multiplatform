package com.gchristov.newsfeed.kmmfeeddata.usecase

import com.gchristov.newsfeed.kmmfeeddata.FeedRepository
import com.gchristov.newsfeed.kmmfeeddata.model.SectionedFeed

class RedecorateSectionedFeedUseCase(
    private val feedRepository: FeedRepository,
    private val flattenSectionedFeedUseCase: FlattenSectionedFeedUseCase,
    private val buildSectionedFeedUseCase: BuildSectionedFeedUseCase
) {
    suspend operator fun invoke(feed: SectionedFeed): SectionedFeed {
        val flattened = flattenSectionedFeedUseCase(feed)
        val redecorated = feedRepository.redecorateFeedPage(flattened)
        return buildSectionedFeedUseCase(redecorated)
    }
}