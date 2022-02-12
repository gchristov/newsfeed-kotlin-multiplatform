package com.gchristov.newsfeed.kmmfeeddata.api

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
}

@Serializable
data class ApiFeedItem(
    val id: String,
    val apiUrl: String,
    val webPublicationDate: String,
    val fields: ApiPostFields? = null,
) {
    @Serializable
    data class ApiPostFields(
        val headline: String? = null,
        val thumbnail: String? = null,
    )
}
