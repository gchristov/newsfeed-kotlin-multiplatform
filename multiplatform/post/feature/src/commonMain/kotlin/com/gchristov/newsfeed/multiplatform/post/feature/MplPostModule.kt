package com.gchristov.newsfeed.multiplatform.post.feature

import com.gchristov.newsfeed.multiplatform.common.kotlin.di.DependencyModule
import kotlinx.coroutines.Dispatchers
import org.kodein.di.DI
import org.kodein.di.bindFactory
import org.kodein.di.instance

object MplPostModule : DependencyModule() {
    override fun name() = "mpl-post"

    override fun bindDependencies(builder: DI.Builder) {
        builder.apply {
//            bindProvider { provideDecoratePostUseCase(postRepository = instance()) }
            bindFactory { postId: String ->
                PostViewModel(
                    dispatcher = Dispatchers.Main,
                    postId = postId,
                    postRepository = instance(),
                )
            }
        }
    }
}