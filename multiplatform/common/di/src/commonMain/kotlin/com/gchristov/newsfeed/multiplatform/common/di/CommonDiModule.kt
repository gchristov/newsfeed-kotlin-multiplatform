package com.gchristov.newsfeed.multiplatform.common.di

import org.kodein.di.DI

object CommonDiModule : DiModule() {
    override fun name() = "multiplatform-common-di"

    override fun bindLocalDependencies(builder: DI.Builder) {
        // Apply any common DI logic here
    }
}
