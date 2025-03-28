package com.gchristov.newsfeed.multiplatform.feed.data.db

import com.gchristov.newsfeed.multiplatform.feed.data.model.FeedFilter

internal fun FeedFilter.toFeedFilter() = DbFeedFilter(query = query)

internal fun DbFeedFilter.toFeedFilter() = FeedFilter(query = query)