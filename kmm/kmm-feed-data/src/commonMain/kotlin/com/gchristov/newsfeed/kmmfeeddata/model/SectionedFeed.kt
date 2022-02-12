package com.gchristov.newsfeed.kmmfeeddata.model

import kotlinx.datetime.Instant

data class SectionedFeed(
    val pages: Int,
    val currentPage: Int,
    val sections: List<Section>
) {
    sealed class SectionType {
        object ThisWeek : SectionType()
        object LastWeek : SectionType()
        object ThisMonth : SectionType()
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