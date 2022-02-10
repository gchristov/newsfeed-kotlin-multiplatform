package com.gchristov.newsfeed.commoncompose.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.gchristov.newsfeed.commoncompose.R

@Immutable
data class ContentColors(
    val action: Color,
    val primary: Color,
    val secondary: Color,
    val warning: Color,
)

internal val LocalContentColors = staticCompositionLocalOf {
    ContentColors(
        action = Color.Unspecified,
        primary = Color.Unspecified,
        secondary = Color.Unspecified,
        warning = Color.Unspecified,
    )
}

@Composable
internal fun lightContentColors() = ContentColors(
    action = colorResource(R.color.red),
    primary = colorResource(R.color.black),
    secondary = colorResource(R.color.black_3),
    warning = colorResource(R.color.yellow),
)

@Composable
internal fun darkContentColors() = ContentColors(
    action = colorResource(R.color.red),
    primary = colorResource(R.color.white),
    secondary = colorResource(R.color.gray_3),
    warning = colorResource(R.color.yellow),
)