package com.gchristov.newsfeed.commoncomposetest

import org.junit.Rule

open class CommonComposeTestClass {
    @get:Rule
    val composeRule = createCustomComposeRule()
}