package com.gchristov.newsfeed.android.common.navigation

import android.content.Context
import com.gchristov.newsfeed.multiplatform.common.kotlin.di.DependencyModule
import org.kodein.di.DI
import org.kodein.di.bindFactory

object NavigationModule : DependencyModule() {
    override fun name() = "common-navigation"

    override fun bindDependencies(builder: DI.Builder) {
        builder.apply {
            bindFactory<Context, Navigator> { context: Context ->
                RealNavigator(context = context)
            }
        }
    }
}