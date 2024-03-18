package com.gchristov.newsfeed.android.app

import android.app.Application
import com.gchristov.newsfeed.android.common.navigation.NavigationModule
import com.gchristov.newsfeed.multiplatform.common.kotlin.AppContext
import com.gchristov.newsfeed.multiplatform.umbrella.di.MplNewsfeedDi

class NewsfeedApp : Application() {
    override fun onCreate() {
        super.onCreate()
        setupDependencyInjection()
    }

    private fun setupDependencyInjection() {
        AppContext = this
        MplNewsfeedDi.setup(appModules = listOf(NavigationModule.module))
    }
}