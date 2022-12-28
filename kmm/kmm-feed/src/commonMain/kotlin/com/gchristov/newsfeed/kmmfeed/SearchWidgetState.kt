package com.gchristov.newsfeed.kmmfeed

// We can model this with a sealed class and pass in the current value directly, eg:
//
// sealed class SearchBarState {
//     object Closed: SearchBarState()
//     data class Opened(val text: String)
// }
//
// TODO: Remodel this and move to AppSearchBar
enum class SearchWidgetState {
    OPENED,
    CLOSED
}