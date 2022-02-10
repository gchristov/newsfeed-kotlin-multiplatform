package com.gchristov.newsfeed.commoncompose.elements.avatar

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.gchristov.newsfeed.commoncompose.R
import java.util.*

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
        colorResource(R.color.red),
        colorResource(R.color.red_2),
        colorResource(R.color.red_3),
        colorResource(R.color.blue),
        colorResource(R.color.blue_2),
        colorResource(R.color.blue_3),
        colorResource(R.color.blue_4),
        colorResource(R.color.blue_5),
        colorResource(R.color.blue_6),
        colorResource(R.color.green),
        colorResource(R.color.green_2),
        colorResource(R.color.green_3),
        colorResource(R.color.green_4),
        colorResource(R.color.yellow),
        colorResource(R.color.yellow_2),
        colorResource(R.color.yellow_3),
        colorResource(R.color.yellow_4),
        colorResource(R.color.yellow_5),
        colorResource(R.color.brown),
        colorResource(R.color.gray),
        colorResource(R.color.gray_2),
        colorResource(R.color.gray_3),
    )
}