package com.gchristov.newsfeed.multiplatform.post.data.api

import kotlinx.serialization.Serializable

@Serializable
data class ApiPostResponse(
    val response: ApiPostDetails,
) {
    @Serializable
    data class ApiPostDetails(val content: ApiPost)
}

@Serializable
data class ApiPost(
    val id: String,
    val webPublicationDate: String,
    val fields: ApiPostFields? = null,
) {
    @Serializable
    data class ApiPostFields(
        val headline: String? = null,
        val body: String? = null,
        val thumbnail: String? = null,
    )
}
