package com.gchristov.newsfeed.commoncompose.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Immutable
data class Typography(
    val title: TextStyle,
    val subtitle: TextStyle,
    val body: TextStyle,
    val bodyBold: TextStyle,
    val caption: TextStyle,
)

internal val LocalTypography = staticCompositionLocalOf {
    Typography(
        title = TextStyle.Default,
        subtitle = TextStyle.Default,
        body = TextStyle.Default,
        bodyBold = TextStyle.Default,
        caption = TextStyle.Default,
    )
}

internal fun typography() = Typography(
    title = Title,
    subtitle = Subtitle,
    body = Body,
    bodyBold = BodyBold,
    caption = Caption,
)

private val Title = TextStyle(fontSize = 24.sp)
private val Subtitle = TextStyle(fontSize = 15.sp)
private val Body = TextStyle(fontSize = 18.sp)
private val BodyBold = Body.copy(fontWeight = FontWeight.Bold)
private val Caption = TextStyle(fontSize = 12.sp)
