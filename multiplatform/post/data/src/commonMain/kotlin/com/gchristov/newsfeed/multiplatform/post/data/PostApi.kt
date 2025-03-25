package com.gchristov.newsfeed.multiplatform.post.data

import arrow.core.Either
import com.gchristov.newsfeed.multiplatform.common.network.NetworkClient
import com.gchristov.newsfeed.multiplatform.common.network.NetworkConfig
import com.gchristov.newsfeed.multiplatform.post.data.api.ApiPostResponse
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header

internal class PostApi(
    private val client: NetworkClient.Json,
    private val config: NetworkConfig,
) {
    suspend fun post(
        postUrl: String,
        postMetadataFields: String,
    ): Either<Throwable, ApiPostResponse> {
        return try {
            val rsp =
                client.http.get("${config.guardianApiUrl}/$postUrl?show-fields=$postMetadataFields") {
                    header("api-key", config.guardianApiKey)
                }.body<ApiPostResponse>()
            Either.Right(rsp)
        } catch (error: Throwable) {
            Either.Left(error)
        }
    }
}