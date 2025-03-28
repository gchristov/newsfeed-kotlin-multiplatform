package com.gchristov.newsfeed.multiplatform.post.data

import com.gchristov.newsfeed.multiplatform.common.kotlin.di.DependencyModule
import com.gchristov.newsfeed.multiplatform.common.network.NetworkClient
import com.gchristov.newsfeed.multiplatform.common.network.NetworkConfig
import com.gchristov.newsfeed.multiplatform.common.persistence.SqlDriverProperties
import com.gchristov.newsfeed.multiplatform.post.data.usecase.EstimateReadingTimeMinutesUseCase
import com.gchristov.newsfeed.multiplatform.post.data.usecase.RealEstimateReadingTimeMinutesUseCase
import com.russhwolf.settings.Settings
import dev.gitlive.firebase.analytics.FirebaseAnalytics
import kotlinx.coroutines.Dispatchers
import org.kodein.di.DI
import org.kodein.di.bindProvider
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
                    ),
                    estimateReadingTimeMinutesUseCase = instance(),
                    analytics = instance()
                )
            }
            bindProvider { provideEstimateReadingTimeMinutesUseCase() }
        }
    }

    private fun providePostRepository(
        api: PostApi,
        sharedPreferences: Settings,
        database: PostSqlDelightDatabase,
        estimateReadingTimeMinutesUseCase: EstimateReadingTimeMinutesUseCase,
        analytics: FirebaseAnalytics
    ): PostRepository = RealPostRepository(
        dispatcher = Dispatchers.Default,
        apiService = api,
        sharedPreferences = sharedPreferences,
        database = database,
        estimateReadingTimeMinutesUseCase = estimateReadingTimeMinutesUseCase,
        analytics = analytics
    )

    private fun providePostApi(
        networkClient: NetworkClient.Json,
        networkConfig: NetworkConfig,
    ) = PostApi(
        client = networkClient,
        config = networkConfig,
    )

    private fun provideEstimateReadingTimeMinutesUseCase(): EstimateReadingTimeMinutesUseCase =
        RealEstimateReadingTimeMinutesUseCase(Dispatchers.Default)
}
