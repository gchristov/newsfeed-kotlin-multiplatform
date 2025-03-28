package com.gchristov.newsfeed.multiplatform.feed.data.model

data class FeedFilter(
    val query: String
) {
    companion object {
        val Default = FeedFilter(query = "fintech")
    }
}
