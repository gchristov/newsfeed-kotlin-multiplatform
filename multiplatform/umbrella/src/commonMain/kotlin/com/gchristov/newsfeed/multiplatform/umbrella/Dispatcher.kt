package com.gchristov.newsfeed.multiplatform.umbrella

import kotlinx.coroutines.Dispatchers

/**
 * Exposes coroutine dispatchers to native targets,
 *
 * Exposing the coroutines dependency directly trigger some link errors so we use a simple wrapper.
 */
@Suppress("unused")
object Dispatcher {
    @Suppress("unused")
    val Default = Dispatchers.Default

    @Suppress("unused")
    val Main = Dispatchers.Main

    @Suppress("unused")
    val Unconfined = Dispatchers.Unconfined
}