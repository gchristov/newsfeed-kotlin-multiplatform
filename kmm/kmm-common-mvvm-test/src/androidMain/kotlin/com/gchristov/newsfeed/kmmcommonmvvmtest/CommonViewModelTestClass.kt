package com.gchristov.newsfeed.kmmcommonmvvmtest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
actual open class CommonViewModelTestClass {
    // Allows testing of LiveData
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        // Allows testing of Dispatchers.Main
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}