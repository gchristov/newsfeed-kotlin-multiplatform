package com.gchristov.newsfeed.kmmcommonnetwork

import io.ktor.client.*

/**
 * Client for generic HTTP requests.
 */
class NetworkClient(val http: HttpClient)

/**
 * Client for API requests.
 */
class ApiClient(val http: HttpClient)