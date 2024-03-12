package com.gchristov.newsfeed.android.common.compose.elements

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.gchristov.newsfeed.android.common.compose.theme.Theme

@Composable
fun AppText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Theme.contentColors.primary,
    textAlign: TextAlign? = null,
    style: TextStyle = Theme.typography.body,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
) {
    androidx.compose.material.Text(
        text = AnnotatedString(text),
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        style = style,
        maxLines = maxLines,
        overflow = overflow
    )
}