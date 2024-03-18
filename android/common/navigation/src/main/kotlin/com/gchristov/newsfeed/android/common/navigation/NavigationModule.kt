package com.gchristov.newsfeed.android.common.navigation

import android.content.Context
import com.gchristov.newsfeed.multiplatform.common.kotlin.di.DiGraph
import com.gchristov.newsfeed.multiplatform.common.kotlin.di.DiModule
import com.gchristov.newsfeed.multiplatform.common.kotlin.di.inject
import org.kodein.di.DI
import org.kodein.di.bindFactory

object NavigationModule : DiModule() {
    override fun name() = "common-navigation"

    override fun bindDependencies(builder: DI.Builder) {
        builder.apply {
            bindFactory<Context, Navigator> { context: Context ->
                RealNavigator(context = context)
            }
        }
    }

    fun injectNavigator(context: Context): Navigator = DiGraph.inject(arg = context)
}