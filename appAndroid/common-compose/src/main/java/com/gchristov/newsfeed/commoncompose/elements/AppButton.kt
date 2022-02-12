package com.gchristov.newsfeed.commoncompose.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.gchristov.newsfeed.commoncompose.theme.Theme
import com.gchristov.newsfeed.commondesign.R

@Composable
fun AppButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String
) {
    androidx.compose.material.Button(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Theme.contentColors.action
        )
    ) {
        AppText(
            text = text,
            color = colorResource(R.color.white)
        )
    }
}

@Composable
fun AppIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector,
    tint: Color = Theme.contentColors.primary,
    contentDescription: String? = null,
) {
    androidx.compose.material.IconButton(
        onClick = onClick,
        modifier = modifier,
        interactionSource = remember { MutableInteractionSource() },
    ) {
        AppIcon(
            imageVector = icon,
            tint = tint,
            contentDescription = contentDescription
        )
    }
}

@Composable
fun AppTextButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String
) {
    AppText(
        modifier = modifier
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        text = text,
        color = Theme.contentColors.action,
    )
}