package com.gchristov.newsfeed.commoncompose.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Immutable
data class Shapes(
    val surface: Shape,
)

internal val LocalShapes = staticCompositionLocalOf {
    Shapes(
        surface = RoundedCornerShape(ZeroCornerSize),
    )
}

internal fun shapes() = Shapes(
    surface = RoundedCornerShape(size = 10.dp)
)