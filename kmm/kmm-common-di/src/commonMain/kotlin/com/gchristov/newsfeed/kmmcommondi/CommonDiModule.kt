package com.gchristov.newsfeed.kmmcommondi

import org.kodein.di.DI

object CommonDiModule : DiModule() {
    override fun name() = "kmm-common-di"

    override fun build(builder: DI.Builder) {
        // Apply any common DI logic here
    }
}
