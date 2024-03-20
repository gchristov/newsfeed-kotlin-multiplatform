package com.gchristov.newsfeed.multiplatform.common.kotlin.di

import org.kodein.di.DI

abstract class DependencyModule {
    abstract fun name(): String

    abstract fun bindDependencies(builder: DI.Builder)

    val module: DI.Module
        get() = DI.Module(name = name()) {
            bindDependencies(builder = this)
        }
}
