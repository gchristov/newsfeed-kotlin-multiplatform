package com.gchristov.newsfeed.kmmpost

import com.gchristov.newsfeed.kmmcommondi.DiModule
import com.gchristov.newsfeed.kmmcommondi.inject
import com.gchristov.newsfeed.kmmfeeddata.FeedDataModule
import org.kodein.di.DI
import org.kodein.di.bindFactory
import org.kodein.di.instance

object PostModule : DiModule() {
    override fun name() = "kmm-post"

    override fun build(builder: DI.Builder) {
        builder.apply {
            bindFactory { postId: String ->
                PostViewModel(
                    postId = postId,
                    feedRepository = instance(),
                )
            }
        }
    }

    override fun dependencies(): List<DI.Module> {
        return listOf(FeedDataModule.module)
    }

    fun injectPostViewModel(postId: String): PostViewModel = inject(arg = postId)
}
