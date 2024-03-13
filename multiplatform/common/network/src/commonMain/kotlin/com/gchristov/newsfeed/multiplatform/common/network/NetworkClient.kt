package com.gchristov.newsfeed.multiplatform.common.network

import io.ktor.client.HttpClient

/**
 * Client for generic HTTP requests.
 */
class NetworkClient(val http: HttpClient)

/**
 * Client for API requests.
 */
class ApiClient(val http: HttpClient)