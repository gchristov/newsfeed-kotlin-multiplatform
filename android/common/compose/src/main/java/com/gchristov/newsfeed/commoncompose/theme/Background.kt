package com.gchristov.newsfeed.commoncompose.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.gchristov.newsfeed.commoncompose.R

@Immutable
data class Backgrounds(
    val primary: Color,
    val surface: Color,
)

internal val LocalBackgrounds = staticCompositionLocalOf {
    Backgrounds(
        primary = Color.Unspecified,
        surface = Color.Unspecified,
    )
}

@Composable
internal fun lightBackgrounds() = Backgrounds(
    primary = colorResource(R.color.gray_4),
    surface = colorResource(R.color.white),
)

@Composable
internal fun darkBackgrounds() = Backgrounds(
    primary = colorResource(R.color.black),
    surface = colorResource(R.color.black_2),
)