package com.gchristov.newsfeed.kmmfeeddata

import com.gchristov.newsfeed.kmmcommondi.CommonDiModule
import com.gchristov.newsfeed.kmmcommondi.DiModule
import com.gchristov.newsfeed.kmmcommonnetwork.ApiClient
import com.gchristov.newsfeed.kmmcommonnetwork.CommonNetworkModule
import com.gchristov.newsfeed.kmmcommonpersistence.CommonPersistenceModule
import com.gchristov.newsfeed.kmmcommonpersistence.SqlDriverProperties
import com.russhwolf.settings.Settings
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

object FeedDataModule : DiModule() {
    override fun name() = "kmm-feed-data"

    override fun build(builder: DI.Builder) {
        builder.apply {
            bindSingleton { provideFeedApi(client = instance()) }
            bindSingleton {
                provideFeedRepository(
                    api = instance(),
                    sharedPreferences = instance(),
                    database = FeedSqlDelightDatabase(
                        instance(
                            arg = SqlDriverProperties(
                                schema = FeedSqlDelightDatabase.Schema,
                                databaseName = "test.db"
                            )
                        )
                    )
                )
            }
        }
    }

    override fun dependencies(): List<DI.Module> {
        return listOf(
            CommonDiModule.module,
            CommonNetworkModule.module,
            CommonPersistenceModule.module
        )
    }

    private fun provideFeedRepository(
        api: FeedApi,
        sharedPreferences: Settings,
        database: FeedSqlDelightDatabase
    ): FeedRepository = RealFeedRepository(
        apiService = api,
        sharedPreferences = sharedPreferences,
        database = database
    )

    private fun provideFeedApi(client: ApiClient) = FeedApi(client)
}
