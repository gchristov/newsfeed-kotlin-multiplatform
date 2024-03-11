package com.gchristov.newsfeed.multiplatform.post.feature

import com.gchristov.newsfeed.kmmcommondi.DiModule
import com.gchristov.newsfeed.kmmcommondi.inject
import com.gchristov.newsfeed.multiplatform.post.data.PostDataModule
import com.gchristov.newsfeed.multiplatform.post.data.PostRepository
import com.gchristov.newsfeed.multiplatform.post.data.usecase.DecoratePostUseCase
import kotlinx.coroutines.Dispatchers
import org.kodein.di.DI
import org.kodein.di.bindFactory
import org.kodein.di.bindProvider
import org.kodein.di.instance

object PostModule : DiModule() {
    override fun name() = "multiplatform-post"

    override fun bindLocalDependencies(builder: DI.Builder) {
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

    override fun moduleDependencies(): List<DI.Module> {
        return listOf(
            PostDataModule.module
        )
    }

    fun injectPostViewModel(postId: String): PostViewModel = inject(arg = postId)
}