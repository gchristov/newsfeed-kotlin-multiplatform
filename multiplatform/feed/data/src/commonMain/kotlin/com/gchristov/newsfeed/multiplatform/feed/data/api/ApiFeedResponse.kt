package com.gchristov.newsfeed.multiplatform.feed.data.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiFeedResponse(
    @SerialName("response") val response: ApiFeed,
) {
    @Serializable
    data class ApiFeed(
        @SerialName("pages") val pages: Int,
        @SerialName("currentPage") val currentPage: Int,
        @SerialName("results") val results: List<ApiFeedItem>
    )

    @Serializable
    data class ApiFeedItem(
        @SerialName("id") val id: String,
        @SerialName("apiUrl") val apiUrl: String,
        @SerialName("webPublicationDate") val webPublicationDate: String,
        @SerialName("fields") val fields: ApiFeedItemFields? = null,
    )

    @Serializable
    data class ApiFeedItemFields(
        @SerialName("headline") val headline: String? = null,
        @SerialName("thumbnail") val thumbnail: String? = null,
    )
}
