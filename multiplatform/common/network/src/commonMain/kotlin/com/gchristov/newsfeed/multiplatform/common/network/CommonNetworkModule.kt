package com.gchristov.newsfeed.multiplatform.common.network

import co.touchlab.kermit.Logger
import com.gchristov.newsfeed.multiplatform.common.kotlin.JsonSerializer
import com.gchristov.newsfeed.multiplatform.common.kotlin.di.DiModule
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

object CommonNetworkModule : DiModule() {
    override fun name() = "multiplatform-network-module"

    override fun bindDependencies(builder: DI.Builder) {
        builder.apply {
            bindSingleton {
                provideJsonNetworkClient(
                    log = instance(),
                    jsonSerializer = instance(),
                )
            }
        }
    }

    private fun provideJsonNetworkClient(
        log: Logger,
        jsonSerializer: JsonSerializer.Default,
    ) = NetworkClient.Json(
        log = log,
        jsonSerializer = jsonSerializer,
    )
}
