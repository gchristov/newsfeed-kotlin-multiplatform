package com.gchristov.newsfeed.android.feed.testfixtures

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
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

    fun assertEmptyStateExists() {
        provider.onNodeWithText("No results found. Please\ntry another search term").assertExists()
    }

    fun assertEmptyStateDoesNotExist() {
        provider.onNodeWithText("No results found. Please\ntry another search term")
            .assertDoesNotExist()
    }

    fun assertSectionExists(header: String) {
        provider.onNodeWithText(header).assertExists()
    }

    fun assertSectionDoesNotExist(header: String) {
        provider.onNodeWithText(header).assertDoesNotExist()
    }

    fun assertFeedItemExists(
        title: String,
        date: String,
    ) {
        provider.onNodeWithText(title).assertExists()
        provider.onNodeWithText(date).assertExists()
    }

    fun assertFeedItemDoesNotExist(
        title: String,
        date: String,
    ) {
        provider.onNodeWithText(title).assertDoesNotExist()
        provider.onNodeWithText(date).assertDoesNotExist()
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