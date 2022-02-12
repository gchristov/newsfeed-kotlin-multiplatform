package com.gchristov.newsfeed.commonnavigation

import android.content.Context
import com.gchristov.newsfeed.kmmcommondi.DiModule
import com.gchristov.newsfeed.kmmcommondi.inject
import org.kodein.di.DI
import org.kodein.di.bindFactory

object NavigationModule : DiModule() {
    override fun name() = "common-navigation"

    override fun bindLocalDependencies(builder: DI.Builder) {
        builder.apply {
            bindFactory<Context, Navigator> { context: Context ->
                RealNavigator(context = context)
            }
        }
    }

    fun injectNavigator(context: Context): Navigator = inject(arg = context)
}
