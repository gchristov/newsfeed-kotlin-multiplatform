package com.gchristov.newsfeed.commoncompose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun Theme(
    type: ThemeType = if (isSystemInDarkTheme()) ThemeType.Dark else ThemeType.Light,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalBackgrounds provides type.backgrounds(),
        LocalContentColors provides type.contentColors(),
        LocalTypography provides typography(),
        LocalShapes provides shapes(),
    ) {
        MaterialTheme(content = content)
    }
}

object Theme {
    val backgrounds: Backgrounds
        @Composable
        get() = LocalBackgrounds.current

    val contentColors: ContentColors
        @Composable
        get() = LocalContentColors.current

    val typography: Typography
        @Composable
        get() = LocalTypography.current

    val shapes: Shapes
        @Composable
        get() = LocalShapes.current
}

sealed class ThemeType {
    object Light : ThemeType()
    object Dark : ThemeType()

    @Composable
    fun backgrounds() = when (this) {
        Dark -> darkBackgrounds()
        Light -> lightBackgrounds()
    }

    @Composable
    fun contentColors() = when (this) {
        Dark -> darkContentColors()
        Light -> lightContentColors()
    }
}