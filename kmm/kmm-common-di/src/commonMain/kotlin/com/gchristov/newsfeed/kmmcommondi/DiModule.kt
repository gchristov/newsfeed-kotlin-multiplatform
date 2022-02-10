package com.gchristov.newsfeed.kmmcommondi

import org.kodein.di.DI
import org.kodein.di.LazyDI
import org.kodein.di.direct
import org.kodein.di.instance

abstract class DiModule {
    abstract fun name(): String

    abstract fun build(builder: DI.Builder)

    open fun dependencies(): List<DI.Module> = emptyList()

    val module: DI.Module
        get() = DI.Module(name = name()) {
            dependencies().forEach { importOnce(it) }
            build(builder = this)
        }

    @PublishedApi
    internal val injector: LazyDI
        get() = DI.lazy {
            importOnce(module)
        }
}

inline fun <reified T : Any> DiModule.inject(): T = injector.direct.instance()

inline fun <reified A : Any, reified T : Any> DiModule.inject(arg: A): T =
    injector.direct.instance(arg = arg)