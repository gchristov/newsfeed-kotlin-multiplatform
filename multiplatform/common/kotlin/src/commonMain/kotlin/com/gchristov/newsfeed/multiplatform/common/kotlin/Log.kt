package com.gchristov.newsfeed.multiplatform.common.kotlin

import co.touchlab.kermit.Logger

fun Logger.debug(tag: String?, message: String) = d(logMessage(tag, message))

fun Logger.debug(tag: String?, throwable: Throwable, message: () -> String) = d(throwable) { logMessage(tag, message()) }

fun Logger.error(tag: String?, throwable: Throwable, message: () -> String) = e(throwable) { logMessage(tag, message()) }

private fun logMessage(tag: String?, message: String) = "[${tag ?: "Anonymous"}] $message"