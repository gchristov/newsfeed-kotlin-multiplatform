package com.gchristov.newsfeed.multiplatform.feed.data.db

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class DbFeedFilter(
    @SerialName("query") val query: String,
)