package com.gchristov.newsfeed.commoncompose.elements

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gchristov.newsfeed.commoncompose.theme.Theme

data class BlockingError(
    val title: String,
    val subtitle: String,
    val retry: String
)

data class NonBlockingError(
    val title: String,
    val subtitle: String,
    val retry: String
)

fun Throwable.toUiBlockingError() = BlockingError(
    title = "Oups!",
    subtitle = "Something has gone wrong. Please try again.",
    retry = "Try again"
)

fun Throwable.toUiNonBlockingError() = NonBlockingError(
    title = "Oups",
    subtitle = "Something went wrong. Please try again.",
    retry = "Retry"
)

@Composable
fun AppBlockingError(
    blockingError: BlockingError,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(0.5f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                AppText(
                    textAlign = TextAlign.Center,
                    text = blockingError.title,
                    style = Theme.typography.title,
                )
                AppText(
                    textAlign = TextAlign.Center,
                    text = blockingError.subtitle,
                    style = Theme.typography.subtitle,
                    color = Theme.contentColors.secondary
                )
            }
            AppButton(
                text = blockingError.retry,
                onClick = onRetry,
            )
        }
    }
}

@Composable
fun AppNonBlockingError(
    nonBlockingError: NonBlockingError,
    onRetry: () -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        AppSurface(elevation = 10.dp) {
            Row(modifier = Modifier.fillMaxWidth()) {
                AppIcon(
                    modifier = Modifier.padding(end = 16.dp, top = 4.dp),
                    imageVector = Icons.Filled.Warning,
                    tint = Theme.contentColors.warning
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    AppText(
                        text = nonBlockingError.title,
                        style = Theme.typography.title,
                    )
                    AppText(
                        text = nonBlockingError.subtitle,
                        style = Theme.typography.subtitle,
                        color = Theme.contentColors.secondary
                    )
                    AppTextButton(
                        onClick = onRetry,
                        text = nonBlockingError.retry
                    )
                }
                AppIconButton(
                    modifier = Modifier.offset(x = 8.dp, y = (-8).dp),
                    icon = Icons.Filled.Clear,
                    onClick = onDismiss,
                    iconTint = Theme.contentColors.action
                )
            }
        }
    }
}