package com.gchristov.newsfeed.kmmpost

import com.gchristov.newsfeed.kmmcommondi.DiModule
import com.gchristov.newsfeed.kmmcommondi.inject
import com.gchristov.newsfeed.kmmpostdata.PostDataModule
import kotlinx.coroutines.Dispatchers
import org.kodein.di.DI
import org.kodein.di.bindFactory
import org.kodein.di.instance

object PostModule : DiModule() {
    override fun name() = "kmm-post"

    override fun bindLocalDependencies(builder: DI.Builder) {
        builder.apply {
            bindFactory { postId: String ->
                PostViewModel(
                    dispatcher = Dispatchers.Main,
                    postId = postId,
                    postRepository = instance(),
                )
            }
        }
    }

    override fun moduleDependencies(): List<DI.Module> {
        return listOf(
            PostDataModule.module
        )
    }

    fun injectPostViewModel(postId: String): PostViewModel = inject(arg = postId)
}
