package com.gchristov.newsfeed.kmmfeeddata.api

import kotlinx.serialization.Serializable

@Serializable
data class ApiFeedResponse(
    val posts: List<ApiPost>,
    val paging: ApiPageCursor
)

@Serializable
data class ApiPost(
    val uid: String,
    val author: String,
    val title: String,
    val body: String? = null,
    val pageId: String? = null,
    val nextPageId: String? = null,
)

@Serializable
data class ApiPageCursor(val next_cursor: String)
