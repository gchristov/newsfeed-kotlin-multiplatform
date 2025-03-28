package com.gchristov.newsfeed.multiplatform.feed.data

import arrow.core.Either
import com.gchristov.newsfeed.multiplatform.common.network.NetworkClient
import com.gchristov.newsfeed.multiplatform.common.network.NetworkConfig
import com.gchristov.newsfeed.multiplatform.feed.data.api.ApiFeedResponse
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header

internal class FeedApi(
    private val client: NetworkClient.Json,
    private val config: NetworkConfig,
) {
    suspend fun feed(
        pageId: Int,
        feedQuery: String
    ): Either<Throwable, ApiFeedResponse> {
        return try {
            val rsp = client.http.get(
                "${config.guardianApiUrl}/search?"
                        + "order-by=newest"
                        + "&show-fields=headline,thumbnail"
                        + "&page-size=20"
                        + "&page=$pageId"
                        + "&q=$feedQuery"
            ) {
                header("api-key", config.guardianApiKey)
            }.body<ApiFeedResponse>()
            Either.Right(rsp)
        } catch (error: Throwable) {
            Either.Left(error)
        }
    }
}