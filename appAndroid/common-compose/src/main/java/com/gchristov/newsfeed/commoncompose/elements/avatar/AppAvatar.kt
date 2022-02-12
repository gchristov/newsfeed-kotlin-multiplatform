package com.gchristov.newsfeed.commoncompose.elements.avatar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import com.gchristov.newsfeed.commoncompose.elements.AppText
import com.gchristov.newsfeed.commoncompose.theme.Theme
import com.gchristov.newsfeed.commondesign.R

@Composable
fun AppAvatar(
    size: Dp,
    text: String,
    textStyle: TextStyle = Theme.typography.body
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(colorGenerator.getColor()),
        contentAlignment = Alignment.Center
    ) {
        AppText(
            text = text.first().toString().uppercase(),
            color = colorResource(R.color.white),
            style = textStyle
        )
    }
}

private val colorGenerator = ColorGenerator()