package com.gchristov.newsfeed.android.common.compose.elements.avatar

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import java.util.Collections
import java.util.Stack
import com.gchristov.newsfeed.android.common.design.R as DesignR

internal class ColorGenerator {
    private val recycle = Stack<Color>()
    private val colors = Stack<Color>()

    @Composable
    fun getColor(): Color {
        if (recycle.isEmpty()) {
            recycle.addAll(palette())
        }
        if (colors.size == 0) {
            while (!recycle.isEmpty()) colors.push(recycle.pop())
            Collections.shuffle(colors)
        }
        val color = colors.pop()
        recycle.push(color)
        return color
    }

    @Composable
    private fun palette() = listOf(
        colorResource(DesignR.color.red),
        colorResource(DesignR.color.red_2),
        colorResource(DesignR.color.red_3),
        colorResource(DesignR.color.blue),
        colorResource(DesignR.color.blue_2),
        colorResource(DesignR.color.blue_3),
        colorResource(DesignR.color.blue_4),
        colorResource(DesignR.color.blue_5),
        colorResource(DesignR.color.blue_6),
        colorResource(DesignR.color.green),
        colorResource(DesignR.color.green_2),
        colorResource(DesignR.color.green_3),
        colorResource(DesignR.color.green_4),
        colorResource(DesignR.color.yellow),
        colorResource(DesignR.color.yellow_2),
        colorResource(DesignR.color.yellow_3),
        colorResource(DesignR.color.yellow_4),
        colorResource(DesignR.color.yellow_5),
        colorResource(DesignR.color.brown),
        colorResource(DesignR.color.gray),
        colorResource(DesignR.color.gray_2),
        colorResource(DesignR.color.gray_3),
    )
}