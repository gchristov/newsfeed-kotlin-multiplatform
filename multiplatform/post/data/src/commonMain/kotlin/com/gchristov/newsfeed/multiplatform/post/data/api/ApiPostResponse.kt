package com.gchristov.newsfeed.multiplatform.post.data.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiPostResponse(
    @SerialName("response") val response: ApiPostDetails,
) {
    @Serializable
    data class ApiPostDetails(@SerialName("content") val content: ApiPost)

    @Serializable
    data class ApiPost(
        @SerialName("id") val id: String,
        @SerialName("webPublicationDate") val webPublicationDate: String,
        @SerialName("fields") val fields: ApiPostFields? = null,
    )

    @Serializable
    data class ApiPostFields(
        @SerialName("headline") val headline: String? = null,
        @SerialName("body") val body: String? = null,
        @SerialName("thumbnail") val thumbnail: String? = null,
    )
}
