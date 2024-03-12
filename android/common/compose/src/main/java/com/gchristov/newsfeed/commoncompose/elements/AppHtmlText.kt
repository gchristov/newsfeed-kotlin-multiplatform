package com.gchristov.newsfeed.commoncompose.elements

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.gchristov.newsfeed.commoncompose.theme.Theme
import com.ireward.htmlcompose.HtmlText

@Composable
fun AppHtmlText(
    modifier: Modifier = Modifier,
    html: String,
    color: Color = Theme.contentColors.primary,
    style: TextStyle = Theme.typography.body.copy(color = color)
) {
    HtmlText(
        modifier = modifier,
        text = html,
        style = style,
    )
}