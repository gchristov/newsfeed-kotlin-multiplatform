package com.gchristov.newsfeed.multiplatform.post.data

import com.gchristov.newsfeed.multiplatform.common.network.ApiClient
import com.gchristov.newsfeed.multiplatform.post.data.api.ApiPostResponse
import io.ktor.client.request.get

internal class PostApi(private val client: ApiClient) {
    suspend fun post(
        postUrl: String,
        postMetadataFields: String
    ): ApiPostResponse = client.http.get(
        "$postUrl?"
                + "show-fields=$postMetadataFields"
    )
}