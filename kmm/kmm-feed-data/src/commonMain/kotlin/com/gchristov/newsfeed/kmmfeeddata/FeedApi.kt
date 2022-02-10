package com.gchristov.newsfeed.kmmfeeddata

import com.gchristov.newsfeed.kmmcommonnetwork.ApiClient
import com.gchristov.newsfeed.kmmfeeddata.api.ApiFeedResponse
import io.ktor.client.request.*

internal class FeedApi(private val client: ApiClient) {
    suspend fun feed(cursor: String? = null): ApiFeedResponse =
        client.http.get("posts?after=$cursor")
}