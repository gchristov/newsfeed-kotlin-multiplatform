package com.gchristov.newsfeed.commoncompose.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

@Immutable
data class Typography(
    val title: TextStyle,
    val subtitle: TextStyle,
    val body: TextStyle,
    val caption: TextStyle,
)

internal val LocalTypography = staticCompositionLocalOf {
    Typography(
        title = TextStyle.Default,
        subtitle = TextStyle.Default,
        body = TextStyle.Default,
        caption = TextStyle.Default,
    )
}

internal fun typography() = Typography(
    title = TextStyle(fontSize = 24.sp),
    subtitle = TextStyle(fontSize = 15.sp),
    body = TextStyle(fontSize = 17.sp),
    caption = TextStyle(fontSize = 12.sp),
)