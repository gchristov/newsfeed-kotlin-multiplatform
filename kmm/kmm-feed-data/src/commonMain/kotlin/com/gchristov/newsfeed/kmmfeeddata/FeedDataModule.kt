package com.gchristov.newsfeed.kmmfeeddata

import com.gchristov.newsfeed.kmmcommondi.DiModule
import com.gchristov.newsfeed.kmmcommonnetwork.ApiClient
import com.gchristov.newsfeed.kmmcommonnetwork.CommonNetworkModule
import com.gchristov.newsfeed.kmmcommonpersistence.CommonPersistenceModule
import com.gchristov.newsfeed.kmmcommonpersistence.SqlDriverProperties
import com.gchristov.newsfeed.kmmfeeddata.usecase.BuildSectionedFeedUseCase
import com.gchristov.newsfeed.kmmfeeddata.usecase.FlattenSectionedFeedUseCase
import com.gchristov.newsfeed.kmmfeeddata.usecase.GetSectionedFeedUseCase
import com.gchristov.newsfeed.kmmfeeddata.usecase.MergeSectionedFeedUseCase
import com.gchristov.newsfeed.kmmfeeddata.usecase.RedecorateSectionedFeedUseCase
import com.gchristov.newsfeed.multiplatform.post.data.PostDataModule
import com.gchristov.newsfeed.multiplatform.post.data.PostRepository
import com.russhwolf.settings.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.datetime.Clock
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindSingleton
import org.kodein.di.instance

object FeedDataModule : DiModule() {
    override fun name() = "kmm-feed-data"

    override fun bindLocalDependencies(builder: DI.Builder) {
        builder.apply {
            bindSingleton { provideFeedApi(client = instance()) }
            bindSingleton {
                provideFeedRepository(
                    api = instance(),
                    postRepository = instance(),
                    database = FeedSqlDelightDatabase(
                        instance(
                            arg = SqlDriverProperties(
                                schema = FeedSqlDelightDatabase.Schema,
                                databaseName = "feed.db"
                            )
                        )
                    ),
                    sharedPreferences = instance()
                )
            }
            bindProvider { provideBuildSectionedFeedUseCase() }
            bindProvider { provideMergeSectionedFeedUseCase() }
            bindProvider { provideFlattenSectionedFeedUseCase() }
            bindProvider {
                provideGetSectionedFeedUseCase(
                    feedRepository = instance(),
                    buildSectionedFeedUseCase = instance(),
                    mergeSectionedFeedUseCase = instance()
                )
            }
            bindProvider {
                provideRedecorateSectionedFeedUseCase(
                    feedRepository = instance(),
                    flattenSectionedFeedUseCase = instance(),
                    buildSectionedFeedUseCase = instance()
                )
            }
        }
    }

    override fun moduleDependencies(): List<DI.Module> {
        return listOf(
            CommonNetworkModule.module,
            CommonPersistenceModule.module,
            PostDataModule.module
        )
    }

    private fun provideFeedRepository(
        api: FeedApi,
        postRepository: PostRepository,
        database: FeedSqlDelightDatabase,
        sharedPreferences: Settings
    ): FeedRepository = RealFeedRepository(
        dispatcher = Dispatchers.Default,
        apiService = api,
        postRepository = postRepository,
        database = database,
        sharedPreferences = sharedPreferences
    )

    private fun provideFeedApi(client: ApiClient) = FeedApi(client)

    private fun provideBuildSectionedFeedUseCase() = BuildSectionedFeedUseCase(
        dispatcher = Dispatchers.Default,
        clock = Clock.System
    )

    private fun provideMergeSectionedFeedUseCase() = MergeSectionedFeedUseCase(Dispatchers.Default)

    private fun provideFlattenSectionedFeedUseCase() =
        FlattenSectionedFeedUseCase(Dispatchers.Default)

    private fun provideGetSectionedFeedUseCase(
        feedRepository: FeedRepository,
        buildSectionedFeedUseCase: BuildSectionedFeedUseCase,
        mergeSectionedFeedUseCase: MergeSectionedFeedUseCase
    ) = GetSectionedFeedUseCase(
        feedRepository = feedRepository,
        buildSectionedFeedUseCase = buildSectionedFeedUseCase,
        mergeSectionedFeedUseCase = mergeSectionedFeedUseCase
    )

    private fun provideRedecorateSectionedFeedUseCase(
        feedRepository: FeedRepository,
        flattenSectionedFeedUseCase: FlattenSectionedFeedUseCase,
        buildSectionedFeedUseCase: BuildSectionedFeedUseCase
    ) = RedecorateSectionedFeedUseCase(
        feedRepository = feedRepository,
        flattenSectionedFeedUseCase = flattenSectionedFeedUseCase,
        buildSectionedFeedUseCase = buildSectionedFeedUseCase
    )
}
