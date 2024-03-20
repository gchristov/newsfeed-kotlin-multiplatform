package com.gchristov.newsfeed.android.common.compose.elements

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter

@Composable
fun AppImage(
    modifier: Modifier = Modifier,
    imageUrl: String,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Fit,
) {
    Image(
        modifier = modifier,
        painter = rememberAsyncImagePainter(imageUrl),
        contentDescription = contentDescription,
        contentScale = contentScale,
    )
}