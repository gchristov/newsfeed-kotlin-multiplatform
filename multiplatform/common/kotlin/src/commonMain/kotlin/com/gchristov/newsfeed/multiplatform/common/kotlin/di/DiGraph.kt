package com.gchristov.newsfeed.multiplatform.common.kotlin.di

import org.kodein.di.DI
import org.kodein.di.direct
import org.kodein.di.instance

object DiGraph {
    @PublishedApi
    internal lateinit var di: DI
}

inline fun <reified T : Any> DiGraph.inject(): T = di.direct.instance()

inline fun <reified A : Any, reified T : Any> DiGraph.inject(arg: A): T =
    di.direct.instance(arg = arg)

fun DiGraph.registerModules(modules: List<DI.Module>) {
    di = DI.lazy {
        modules.forEach { importOnce(it) }
    }
}