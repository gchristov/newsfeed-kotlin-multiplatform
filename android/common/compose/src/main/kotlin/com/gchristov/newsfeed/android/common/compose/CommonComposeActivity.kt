package com.gchristov.newsfeed.android.common.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.gchristov.newsfeed.android.common.compose.theme.Theme

abstract class CommonComposeActivity : ComponentActivity() {
    @Composable
    protected abstract fun Content()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Theme {
                Content()
            }
        }
    }
}