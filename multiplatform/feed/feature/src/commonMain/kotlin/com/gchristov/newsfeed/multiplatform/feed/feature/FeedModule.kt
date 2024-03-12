package com.gchristov.newsfeed.multiplatform.feed.feature

import com.gchristov.newsfeed.kmmcommondi.DiModule
import com.gchristov.newsfeed.kmmcommondi.inject
import com.gchristov.newsfeed.multiplatform.feed.data.FeedDataModule
import kotlinx.coroutines.Dispatchers
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance

object FeedModule : DiModule() {
    override fun name() = "multiplatform-feed"

    override fun bindLocalDependencies(builder: DI.Builder) {
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

    override fun moduleDependencies(): List<DI.Module> {
        return listOf(FeedDataModule.module)
    }

    fun injectFeedViewModel(): FeedViewModel = inject()
}