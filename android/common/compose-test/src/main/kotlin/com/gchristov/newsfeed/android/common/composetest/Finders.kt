package com.gchristov.newsfeed.android.common.composetest

import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.hasProgressBarRangeInfo

/* Indeterminate progress */

fun SemanticsNodeInteractionsProvider.onIndeterminateProgress(): SemanticsNodeInteraction {
    return onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate))
}