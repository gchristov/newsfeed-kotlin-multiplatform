package com.gchristov.newsfeed.multiplatform.feed.feature

import com.gchristov.newsfeed.multiplatform.common.kotlin.di.DiGraph
import com.gchristov.newsfeed.multiplatform.common.kotlin.di.DiModule
import com.gchristov.newsfeed.multiplatform.common.kotlin.di.inject
import kotlinx.coroutines.Dispatchers
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance

object FeedModule : DiModule() {
    override fun name() = "multiplatform-feed"

    override fun bindDependencies(builder: DI.Builder) {
        builder.apply {
            bindProvider {
                FeedViewModel(
                    dispatcher = Dispatchers.Main,
                    feedRepository = instance(),
                    getSectionedFeedUseCase = instance(),
                    redecorateSectionedFeedUseCase = instance()
                )
            }
        }
    }

    fun injectFeedViewModel(): FeedViewModel = DiGraph.inject()
}