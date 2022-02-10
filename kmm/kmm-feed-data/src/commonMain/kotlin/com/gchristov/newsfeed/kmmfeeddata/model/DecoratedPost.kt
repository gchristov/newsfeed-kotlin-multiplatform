package com.gchristov.newsfeed.kmmfeeddata.model

import com.gchristov.newsfeed.kmmfeeddata.FeedRepository
import com.gchristov.newsfeed.kmmfeeddata.Post

data class DecoratedPost(
    val post: Post,
    // Additional properties
    val favouriteTimestamp: Long?, // Will be set later if post has been added to favourites
) {
    fun isFavourite() = favouriteTimestamp != null
}

fun Post.decorate(repository: FeedRepository) = DecoratedPost(
    post = this,
    favouriteTimestamp = repository.favouriteTimestamp(postId = uid)
)