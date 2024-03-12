package com.gchristov.newsfeed.commoncompose.elements

import androidx.compose.foundation.Indication
import androidx.compose.runtime.Composable

@Composable
fun rememberRipple(): Indication {
    return androidx.compose.material.ripple.rememberRipple()
}