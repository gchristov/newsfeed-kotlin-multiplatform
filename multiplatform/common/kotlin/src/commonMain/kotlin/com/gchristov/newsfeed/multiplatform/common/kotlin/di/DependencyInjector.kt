package com.gchristov.newsfeed.multiplatform.common.kotlin.di

import org.kodein.di.DI
import org.kodein.di.direct
import org.kodein.di.instance

object DependencyInjector {
    @PublishedApi
    internal lateinit var di: DI
}

inline fun <reified T : Any> DependencyInjector.inject(): T = di.direct.instance()

inline fun <reified A : Any, reified T : Any> DependencyInjector.inject(arg: A): T =
    di.direct.instance(arg = arg)

fun DependencyInjector.register(modules: List<DependencyModule>) {
    di = DI.lazy {
        modules.forEach { importOnce(it.module) }
    }
}