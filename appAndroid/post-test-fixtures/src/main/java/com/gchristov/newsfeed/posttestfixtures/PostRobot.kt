package com.gchristov.newsfeed.posttestfixtures

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.gchristov.newsfeed.commoncomposetest.onIndeterminateProgress

fun SemanticsNodeInteractionsProvider.post(block: PostRobot.() -> Unit) {
    PostRobot(this).block()
}

class PostRobot internal constructor(private val provider: SemanticsNodeInteractionsProvider) {
    fun assertLoadingExists() {
        provider.onIndeterminateProgress().assertExists()
    }

    fun assertLoadingDoesNotExist() {
        provider.onIndeterminateProgress().assertDoesNotExist()
    }

    fun assertPostExists(
        title: String,
        author: String,
        readingTime: String,
        body: String,
    ) {
        provider.onNodeWithText(title).assertExists()
        provider.onNodeWithText(author).assertExists()
        provider.onNodeWithText(readingTime).assertExists()
        provider.onNodeWithText(body).assertExists()
    }

    fun assertPostDoesNotExist(
        title: String,
        author: String,
        readingTime: String,
        body: String,
    ) {
        provider.onNodeWithText(title).assertDoesNotExist()
        provider.onNodeWithText(author).assertDoesNotExist()
        provider.onNodeWithText(readingTime).assertDoesNotExist()
        provider.onNodeWithText(body).assertDoesNotExist()
    }

    fun assertAddToFavouritesButtonExists() {
        provider.onNodeWithContentDescription(AddToFavourite).assertExists()
    }

    fun assertAddToFavouritesButtonDoesNotExist() {
        provider.onNodeWithContentDescription(AddToFavourite).assertDoesNotExist()
    }

    fun assertRemoveFromFavouritesButtonExists() {
        provider.onNodeWithContentDescription(RemoveFromFavourite).assertExists()
    }

    fun assertRemoveFromFavouritesButtonDoesNotExist() {
        provider.onNodeWithContentDescription(RemoveFromFavourite).assertDoesNotExist()
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

    fun clickAddToFavouritesButton() {
        provider.onNodeWithContentDescription(AddToFavourite).performClick()
    }

    fun clickRemoveFromFavouritesButton() {
        provider.onNodeWithContentDescription(RemoveFromFavourite).performClick()
    }
}

private const val AddToFavourite = "Add to favourites"
private const val RemoveFromFavourite = "Remove from favourites"

private const val BlockingErrorTitle = "Oups!"
private const val BlockingErrorSubtitle = "Something has gone wrong. Please try again."
private const val BlockingErrorAction = "Try again"