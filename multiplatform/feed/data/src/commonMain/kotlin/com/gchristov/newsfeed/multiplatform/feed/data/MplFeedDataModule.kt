package com.gchristov.newsfeed.multiplatform.feed.data

import com.gchristov.newsfeed.multiplatform.auth.data.AuthRepository
import com.gchristov.newsfeed.multiplatform.common.kotlin.di.DependencyModule
import com.gchristov.newsfeed.multiplatform.common.network.NetworkClient
import com.gchristov.newsfeed.multiplatform.common.network.NetworkConfig
import com.gchristov.newsfeed.multiplatform.common.persistence.SqlDriverProperties
import com.gchristov.newsfeed.multiplatform.feed.data.usecase.BuildSectionedFeedUseCase
import com.gchristov.newsfeed.multiplatform.feed.data.usecase.FlattenSectionedFeedUseCase
import com.gchristov.newsfeed.multiplatform.feed.data.usecase.MergeSectionedFeedUseCase
import com.gchristov.newsfeed.multiplatform.feed.data.usecase.RealBuildSectionedFeedUseCase
import com.gchristov.newsfeed.multiplatform.feed.data.usecase.RealFlattenSectionedFeedUseCase
import com.gchristov.newsfeed.multiplatform.feed.data.usecase.RealMergeSectionedFeedUseCase
import com.gchristov.newsfeed.multiplatform.feed.data.usecase.RealRedecorateSectionedFeedUseCase
import com.gchristov.newsfeed.multiplatform.feed.data.usecase.RedecorateSectionedFeedUseCase
import com.gchristov.newsfeed.multiplatform.post.data.PostRepository
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.datetime.Clock
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindSingleton
import org.kodein.di.instance

object MplFeedDataModule : DependencyModule() {
    override fun name() = "mpl-feed-data"

    override fun bindDependencies(builder: DI.Builder) {
        builder.apply {
            bindSingleton {
                provideFeedApi(
                    networkClient = instance(),
                    networkConfig = instance(),
                )
            }
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
                    authRepository = instance(),
                    firestore = instance()
                )
            }
            bindProvider { provideBuildSectionedFeedUseCase() }
            bindProvider { provideMergeSectionedFeedUseCase() }
            bindProvider { provideFlattenSectionedFeedUseCase() }
            bindProvider {
                provideRedecorateSectionedFeedUseCase(
                    feedRepository = instance(),
                    flattenSectionedFeedUseCase = instance(),
                    buildSectionedFeedUseCase = instance()
                )
            }
        }
    }

    private fun provideFeedRepository(
        api: FeedApi,
        postRepository: PostRepository,
        database: FeedSqlDelightDatabase,
        authRepository: AuthRepository,
        firestore: FirebaseFirestore
    ): FeedRepository = RealFeedRepository(
        dispatcher = Dispatchers.Default,
        apiService = api,
        postRepository = postRepository,
        database = database,
        authRepository = authRepository,
        firestore = firestore,
    )

    private fun provideFeedApi(
        networkClient: NetworkClient.Json,
        networkConfig: NetworkConfig,
    ) = FeedApi(
        client = networkClient,
        config = networkConfig,
    )

    private fun provideBuildSectionedFeedUseCase(): BuildSectionedFeedUseCase =
        RealBuildSectionedFeedUseCase(
            dispatcher = Dispatchers.Default,
            clock = Clock.System,
        )

    private fun provideMergeSectionedFeedUseCase(): MergeSectionedFeedUseCase =
        RealMergeSectionedFeedUseCase(Dispatchers.Default)

    private fun provideFlattenSectionedFeedUseCase(): FlattenSectionedFeedUseCase =
        RealFlattenSectionedFeedUseCase(Dispatchers.Default)

    private fun provideRedecorateSectionedFeedUseCase(
        feedRepository: FeedRepository,
        flattenSectionedFeedUseCase: FlattenSectionedFeedUseCase,
        buildSectionedFeedUseCase: BuildSectionedFeedUseCase,
    ): RedecorateSectionedFeedUseCase = RealRedecorateSectionedFeedUseCase(
        dispatcher = Dispatchers.Default,
        feedRepository = feedRepository,
        flattenSectionedFeedUseCase = flattenSectionedFeedUseCase,
        buildSectionedFeedUseCase = buildSectionedFeedUseCase
    )
}
