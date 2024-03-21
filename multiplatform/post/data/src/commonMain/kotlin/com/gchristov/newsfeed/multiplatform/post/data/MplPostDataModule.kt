package com.gchristov.newsfeed.multiplatform.post.data

import com.gchristov.newsfeed.multiplatform.common.kotlin.di.DependencyModule
import com.gchristov.newsfeed.multiplatform.common.network.NetworkClient
import com.gchristov.newsfeed.multiplatform.common.network.NetworkConfig
import com.gchristov.newsfeed.multiplatform.common.persistence.SqlDriverProperties
import com.russhwolf.settings.Settings
import kotlinx.coroutines.Dispatchers
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

object MplPostDataModule : DependencyModule() {
    override fun name() = "mpl-post-data"

    override fun bindDependencies(builder: DI.Builder) {
        builder.apply {
            bindSingleton {
                providePostApi(
                    networkClient = instance(),
                    networkConfig = instance(),
                )
            }
            bindSingleton {
                providePostRepository(
                    api = instance(),
                    sharedPreferences = instance(),
                    database = PostSqlDelightDatabase(
                        instance(
                            arg = SqlDriverProperties(
                                schema = PostSqlDelightDatabase.Schema,
                                databaseName = "post.db"
                            )
                        )
                    )
                )
            }
        }
    }

    private fun providePostRepository(
        api: PostApi,
        sharedPreferences: Settings,
        database: PostSqlDelightDatabase
    ): PostRepository = RealPostRepository(
        dispatcher = Dispatchers.Default,
        apiService = api,
        sharedPreferences = sharedPreferences,
        database = database
    )

    private fun providePostApi(
        networkClient: NetworkClient.Json,
        networkConfig: NetworkConfig,
    ) = PostApi(
        client = networkClient,
        config = networkConfig,
    )
}
