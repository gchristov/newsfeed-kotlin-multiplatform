package com.gchristov.newsfeed.kmmpost

import com.gchristov.newsfeed.kmmcommondi.DiModule
import com.gchristov.newsfeed.kmmcommondi.inject
import com.gchristov.newsfeed.kmmpostdata.PostDataModule
import com.gchristov.newsfeed.kmmpostdata.PostRepository
import com.gchristov.newsfeed.kmmpostdata.usecase.DecoratePostUseCase
import kotlinx.coroutines.Dispatchers
import org.kodein.di.DI
import org.kodein.di.bindFactory
import org.kodein.di.bindProvider
import org.kodein.di.instance

object PostModule : DiModule() {
    override fun name() = "kmm-post"

    override fun bindLocalDependencies(builder: DI.Builder) {
        builder.apply {
            //TODO: provider or singleton?
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
