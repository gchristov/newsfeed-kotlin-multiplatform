package com.gchristov.newsfeed.multiplatform.feed.data.api

import kotlinx.serialization.Serializable

@Serializable
data class ApiFeedResponse(
    val response: ApiFeed,
) {
    @Serializable
    data class ApiFeed(
        val pages: Int,
        val currentPage: Int,
        val results: List<ApiFeedItem>
    )

    @Serializable
    data class ApiFeedItem(
        val id: String,
        val apiUrl: String,
        val webPublicationDate: String,
        val fields: ApiFeedItemFields? = null,
    )

    @Serializable
    data class ApiFeedItemFields(
        val headline: String? = null,
        val thumbnail: String? = null,
    )
}
