package com.gchristov.newsfeed.multiplatform.feed.data.model

import kotlinx.datetime.Instant

data class SectionedFeed(
    val pages: Int,
    val currentPage: Int,
    val sections: List<Section>
) {
    sealed class SectionType {
        data object ThisWeek : SectionType()
        data object LastWeek : SectionType()
        data object ThisMonth : SectionType()
        data class Older(
            val date: Instant
        ) : SectionType()
    }

    data class Section(
        val type: SectionType,
        val feedItems: List<DecoratedFeedItem>
    )
}

fun SectionedFeed.hasNextPage() = currentPage < pages