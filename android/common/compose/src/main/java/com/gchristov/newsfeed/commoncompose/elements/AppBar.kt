package com.gchristov.newsfeed.commoncompose.elements

import android.app.Activity
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.gchristov.newsfeed.commoncompose.theme.Theme

@Composable
fun AppBar(
    title: String? = null,
    elevated: Boolean = true,
    showBack: Boolean = false,
    contentColor: Color = Theme.contentColors.primary,
    actions: @Composable (RowScope.() -> Unit)? = null,
) {
    val activity = LocalContext.current as? Activity

    TopAppBar(
        title = {
            title?.let { title ->
                AppText(
                    text = title,
                    color = contentColor
                )
            }
        },
        navigationIcon = if (showBack) {
            @Composable {
                AppIconButton(
                    icon = Icons.Filled.ArrowBack,
                    tint = contentColor,
                    onClick = { activity?.finish() }
                )
            }
        } else null,
        actions = { actions?.invoke(this) },
        backgroundColor = if (elevated) Theme.backgrounds.surface else Color.Transparent,
        elevation = if (elevated) 5.dp else 0.dp,
    )
}
