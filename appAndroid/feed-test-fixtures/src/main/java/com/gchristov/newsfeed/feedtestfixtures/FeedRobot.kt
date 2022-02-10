package com.gchristov.newsfeed.feedtestfixtures

import androidx.compose.ui.test.*
import com.gchristov.newsfeed.commoncomposetest.onIndeterminateProgress

fun SemanticsNodeInteractionsProvider.feed(block: FeedRobot.() -> Unit) {
    FeedRobot(this).block()
}

class FeedRobot internal constructor(private val provider: SemanticsNodeInteractionsProvider) {
    fun assertLoadingExists() {
        provider.onIndeterminateProgress().assertExists()
    }

    fun assertLoadingDoesNotExist() {
        provider.onIndeterminateProgress().assertDoesNotExist()
    }

    fun assertFeedItemExists(
        title: String,
        author: String,
        body: String,
    ) {
        provider.onNodeWithText(title).assertExists()
        provider.onNodeWithText(author).assertExists()
        provider.onNodeWithText(body).assertExists()
    }

    fun assertFeedItemDoesNotExist(
        title: String,
        author: String,
        body: String,
    ) {
        provider.onNodeWithText(title).assertDoesNotExist()
        provider.onNodeWithText(author).assertDoesNotExist()
        provider.onNodeWithText(body).assertDoesNotExist()
    }

    fun assertFavouriteItemsShown(favouriteItems: Int) {
        provider
            .onAllNodesWithContentDescription(AddedToFavourites)
            .assertCountEquals(favouriteItems)
    }

    fun assertBlockingErrorExists() {
        provider.onNodeWithText(BlockingErrorTitle).assertExists()
        provider.onNodeWithText(BlockingErrorSubtitle).assertExists()
        provider.onNodeWithText(BlockingErrorAction).assertExists()
    }

    fun assertBlockingErrorDoesNotExist() {
        provider.onNodeWithText(BlockingErrorTitle).assertDoesNotExist()
        provider.onNodeWithText(BlockingErrorSubtitle).assertDoesNotExist()
        provider.onNodeWithText(BlockingErrorAction).assertDoesNotExist()
    }

    fun assertNonBlockingErrorExists() {
        provider.onNodeWithText(NonBlockingErrorTitle).assertExists()
        provider.onNodeWithText(NonBlockingErrorSubtitle).assertExists()
        provider.onNodeWithText(NonBlockingErrorAction).assertExists()
    }

    fun assertNonBlockingErrorDoesNotExist() {
        provider.onNodeWithText(NonBlockingErrorTitle).assertDoesNotExist()
        provider.onNodeWithText(NonBlockingErrorSubtitle).assertDoesNotExist()
        provider.onNodeWithText(NonBlockingErrorAction).assertDoesNotExist()
    }

    fun clickPost(title: String) {
        provider.onNodeWithText(title).performClick()
    }
}

private const val AddedToFavourites = "Added to favourites"

private const val BlockingErrorTitle = "Oups!"
private const val BlockingErrorSubtitle = "Something has gone wrong. Please try again."
private const val BlockingErrorAction = "Try again"
private const val NonBlockingErrorTitle = "Oups"
private const val NonBlockingErrorSubtitle = "Something went wrong. Please try again."
private const val NonBlockingErrorAction = "Retry"