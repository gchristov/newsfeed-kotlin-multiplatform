package com.gchristov.newsfeed.commoncompose.elements

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberImagePainter

@Composable
fun AppImage(
    modifier: Modifier = Modifier,
    imageUrl: String,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Fit,
) {
    Image(
        modifier = modifier,
        painter = rememberImagePainter(imageUrl),
        contentDescription = contentDescription,
        contentScale = contentScale,
    )
}