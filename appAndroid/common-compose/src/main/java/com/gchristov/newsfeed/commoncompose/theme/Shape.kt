package com.gchristov.newsfeed.commoncompose.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Immutable
data class Shapes(
    val surface: Shape,
    val groupStart: Shape,
    val groupMiddle: Shape,
    val groupEnd: Shape,
    val groupSingle: Shape,
)

internal val LocalShapes = staticCompositionLocalOf {
    Shapes(
        surface = RoundedCornerShape(ZeroCornerSize),
        groupStart = RoundedCornerShape(ZeroCornerSize),
        groupMiddle = RoundedCornerShape(ZeroCornerSize),
        groupEnd = RoundedCornerShape(ZeroCornerSize),
        groupSingle = RoundedCornerShape(ZeroCornerSize),
    )
}

internal fun shapes() = Shapes(
    surface = RoundedCornerShape(size = CornerRadius),
    groupStart = RoundedCornerShape(
        topStart = CornerRadius,
        topEnd = CornerRadius
    ),
    groupMiddle = RectangleShape,
    groupEnd = RoundedCornerShape(
        bottomStart = CornerRadius,
        bottomEnd = CornerRadius
    ),
    groupSingle = RoundedCornerShape(size = CornerRadius),
)

private val CornerRadius = 8.dp