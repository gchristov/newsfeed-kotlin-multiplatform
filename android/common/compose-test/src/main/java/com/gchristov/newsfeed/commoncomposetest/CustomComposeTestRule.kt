package com.gchristov.newsfeed.commoncomposetest

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.unit.Density
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.gchristov.newsfeed.commoncompose.theme.Theme

fun createCustomComposeRule(): CustomComposeTestRule {
    return CustomComposeTestRuleWrapper(createAndroidComposeRule())
}

interface CustomComposeTestRule : ComposeContentTestRule

private class CustomComposeTestRuleWrapper(
    private val delegate: AndroidComposeTestRule<ActivityScenarioRule<ComposeTestActivity>, ComposeTestActivity>
) : CustomComposeTestRule, ComposeContentTestRule by delegate {
    private val nightMode = false
    private val fontScale = 1f

    override fun setContent(composable: @Composable () -> Unit) {
        delegate.setContent {
            // Theme
            val configuration = Configuration(LocalConfiguration.current).apply {
                val nightModeBits = if (nightMode) {
                    Configuration.UI_MODE_NIGHT_YES
                } else {
                    Configuration.UI_MODE_NIGHT_NO
                }
                uiMode = (uiMode and Configuration.UI_MODE_NIGHT_MASK.inv()) or nightModeBits
            }
            // Font size
            val density = Density(
                density = LocalDensity.current.density,
                fontScale = fontScale
            )
            // Content
            CompositionLocalProvider(
                LocalConfiguration provides configuration,
                LocalDensity provides density
            ) {
                Theme {
                    composable()
                }
            }
        }
    }
}