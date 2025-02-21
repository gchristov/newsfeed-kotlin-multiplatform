package com.gchristov.newsfeed.multiplatform.post.data

import arrow.core.Either
import com.gchristov.newsfeed.multiplatform.common.network.NetworkClient
import com.gchristov.newsfeed.multiplatform.common.network.NetworkConfig
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse

internal class PostApi(
    private val client: NetworkClient.Json,
    private val config: NetworkConfig,
) {
    suspend fun post(
        postUrl: String,
        postMetadataFields: String,
    ): Either<Throwable, HttpResponse> {
        return try {
            val rsp =
                client.http.get("${config.guardianApiUrl}/$postUrl?show-fields=$postMetadataFields") {
                    header("api-key", config.guardianApiKey)
                }
            Either.Right(rsp)
        } catch (error: Throwable) {
            Either.Left(error)
        }
    }
}