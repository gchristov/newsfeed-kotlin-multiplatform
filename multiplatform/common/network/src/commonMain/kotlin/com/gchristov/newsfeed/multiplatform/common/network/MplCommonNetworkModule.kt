package com.gchristov.newsfeed.multiplatform.common.network

import co.touchlab.kermit.Logger
import com.gchristov.newsfeed.multiplatform.common.kotlin.JsonSerializer
import com.gchristov.newsfeed.multiplatform.common.kotlin.di.DependencyModule
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

object MplCommonNetworkModule : DependencyModule() {
    override fun name() = "mpl-network-module"

    override fun bindDependencies(builder: DI.Builder) {
        builder.apply {
            bindSingleton { provideNetworkConfig() }
            bindSingleton {
                provideJsonNetworkClient(
                    log = instance(),
                    jsonSerializer = instance(),
                )
            }
        }
    }

    private fun provideNetworkConfig(): NetworkConfig = NetworkConfig(
        guardianApiKey = BuildConfig.GUARDIAN_API_KEY,
        guardianApiUrl = BuildConfig.GUARDIAN_API_URL,
    )

    private fun provideJsonNetworkClient(
        log: Logger,
        jsonSerializer: JsonSerializer.Default,
    ) = NetworkClient.Json(
        log = log,
        jsonSerializer = jsonSerializer,
    )
}
