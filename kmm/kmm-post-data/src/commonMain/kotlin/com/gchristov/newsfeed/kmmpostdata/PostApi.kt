package com.gchristov.newsfeed.kmmpostdata

import com.gchristov.newsfeed.kmmcommonnetwork.ApiClient
import com.gchristov.newsfeed.kmmpostdata.api.ApiPostResponse
import io.ktor.client.request.*

internal class PostApi(private val client: ApiClient) {
    suspend fun post(
        postUrl: String,
        postMetadataFields: String
    ): ApiPostResponse = client.http.get(
        "$postUrl?"
                + "show-fields=$postMetadataFields"
    )
}