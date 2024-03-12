package com.gchristov.newsfeed.multiplatform.post.data

import com.gchristov.newsfeed.multiplatform.common.di.DiModule
import com.gchristov.newsfeed.multiplatform.common.network.ApiClient
import com.gchristov.newsfeed.multiplatform.common.network.CommonNetworkModule
import com.gchristov.newsfeed.multiplatform.common.persistence.CommonPersistenceModule
import com.gchristov.newsfeed.multiplatform.common.persistence.SqlDriverProperties
import com.russhwolf.settings.Settings
import kotlinx.coroutines.Dispatchers
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

object PostDataModule : DiModule() {
    override fun name() = "multiplatform-post-data"

    override fun bindLocalDependencies(builder: DI.Builder) {
        builder.apply {
            bindSingleton { providePostApi(client = instance()) }
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

    override fun moduleDependencies(): List<DI.Module> {
        return listOf(
            CommonNetworkModule.module,
            CommonPersistenceModule.module
        )
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

    private fun providePostApi(client: ApiClient) = PostApi(client)
}
