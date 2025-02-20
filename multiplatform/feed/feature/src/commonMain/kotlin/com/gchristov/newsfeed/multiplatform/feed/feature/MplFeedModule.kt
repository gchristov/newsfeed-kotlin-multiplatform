package com.gchristov.newsfeed.multiplatform.feed.feature

import com.gchristov.newsfeed.multiplatform.common.kotlin.di.DependencyModule
import kotlinx.coroutines.Dispatchers
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance

object MplFeedModule : DependencyModule() {
    override fun name() = "mpl-feed"

    override fun bindDependencies(builder: DI.Builder) {
        builder.apply {
            bindProvider {
                FeedViewModel(
                    dispatcher = Dispatchers.Main,
                    feedRepository = instance(),
                    redecorateSectionedFeedUseCase = instance(),
                    buildSectionedFeedUseCase = instance(),
                    mergeSectionedFeedUseCase = instance(),
                )
            }
        }
    }
}