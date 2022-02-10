package com.gchristov.newsfeed.commoncomposetest

import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.hasProgressBarRangeInfo

/* Indeterminate progress */

fun SemanticsNodeInteractionsProvider.onIndeterminateProgress(): SemanticsNodeInteraction {
    return onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate))
}