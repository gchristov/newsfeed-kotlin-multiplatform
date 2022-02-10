package com.gchristov.newsfeed.kmmfeed

import com.gchristov.newsfeed.kmmcommondi.DiModule
import com.gchristov.newsfeed.kmmcommondi.inject
import com.gchristov.newsfeed.kmmfeeddata.FeedDataModule
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance

object FeedModule : DiModule() {
    override fun name() = "kmm-feed"

    override fun build(builder: DI.Builder) {
        builder.apply {
            bindProvider {
                FeedViewModel(feedRepository = instance())
            }
        }
    }

    override fun dependencies(): List<DI.Module> {
        return listOf(FeedDataModule.module)
    }

    fun injectFeedViewModel(): FeedViewModel = inject()
}
