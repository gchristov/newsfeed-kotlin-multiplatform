package com.gchristov.newsfeed.multiplatform.common.mvvmtest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule

@ExperimentalCoroutinesApi
actual open class CommonViewModelTestClass {
    // Allows testing of LiveData
    @get:Rule
    val rule = InstantTaskExecutorRule()
}