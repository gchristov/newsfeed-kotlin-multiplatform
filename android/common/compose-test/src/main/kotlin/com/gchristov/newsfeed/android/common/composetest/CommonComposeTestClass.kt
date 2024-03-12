package com.gchristov.newsfeed.android.common.composetest

import org.junit.Rule

open class CommonComposeTestClass {
    @get:Rule
    val composeRule = createCustomComposeRule()
}