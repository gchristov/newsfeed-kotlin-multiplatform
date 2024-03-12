package com.gchristov.newsfeed.android.app

import android.app.Application
import com.gchristov.newsfeed.multiplatform.common.di.AppContext

class NewsfeedApp : Application() {
    override fun onCreate() {
        super.onCreate()
        setupDependencyInjection()
    }

    private fun setupDependencyInjection() {
        AppContext = this
    }
}