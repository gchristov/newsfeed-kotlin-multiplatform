package com.gchristov.newsfeed.multiplatform.post.feature

import com.gchristov.newsfeed.multiplatform.common.kotlin.di.DependencyModule
import com.gchristov.newsfeed.multiplatform.post.data.PostRepository
import com.gchristov.newsfeed.multiplatform.post.data.usecase.DecoratePostUseCase
import kotlinx.coroutines.Dispatchers
import org.kodein.di.DI
import org.kodein.di.bindFactory
import org.kodein.di.bindProvider
import org.kodein.di.instance

object MplPostModule : DependencyModule() {
    override fun name() = "mpl-post"

    override fun bindDependencies(builder: DI.Builder) {
        builder.apply {
            bindProvider { provideDecoratePostUseCase(postRepository = instance()) }
            bindFactory { postId: String ->
                PostViewModel(
                    dispatcher = Dispatchers.Main,
                    postId = postId,
                    decoratePostUseCase = instance(),
                    postRepository = instance()
                )
            }
        }
    }

    private fun provideDecoratePostUseCase(postRepository: PostRepository) = DecoratePostUseCase(
        postRepository = postRepository,
        dispatcher = Dispatchers.Main
    )
}