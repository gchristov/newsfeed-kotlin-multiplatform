package com.gchristov.newsfeed.multiplatform.umbrella

import com.gchristov.newsfeed.multiplatform.auth.data.MplAuthDataModule
import com.gchristov.newsfeed.multiplatform.common.firebase.MplCommonFirebaseModule
import com.gchristov.newsfeed.multiplatform.common.kotlin.MplCommonKotlinModule
import com.gchristov.newsfeed.multiplatform.common.kotlin.di.DependencyInjector
import com.gchristov.newsfeed.multiplatform.common.kotlin.di.DependencyModule
import com.gchristov.newsfeed.multiplatform.common.kotlin.di.inject
import com.gchristov.newsfeed.multiplatform.common.kotlin.di.register
import com.gchristov.newsfeed.multiplatform.common.network.MplCommonNetworkModule
import com.gchristov.newsfeed.multiplatform.common.persistence.MplCommonPersistenceModule
import com.gchristov.newsfeed.multiplatform.feed.data.MplFeedDataModule
import com.gchristov.newsfeed.multiplatform.feed.feature.FeedViewModel
import com.gchristov.newsfeed.multiplatform.feed.feature.MplFeedModule
import com.gchristov.newsfeed.multiplatform.post.data.MplPostDataModule
import com.gchristov.newsfeed.multiplatform.post.feature.MplPostModule
import com.gchristov.newsfeed.multiplatform.post.feature.PostViewModel

/**
 * Initialises the dependency graph for native targets.
 *
 * When making changes, consider also updating android/app module.
 */
@Suppress("unused")
fun DependencyInjector.initialise() {
    val modules = mutableListOf<DependencyModule>().apply {
        add(MplAuthDataModule)
        add(MplCommonKotlinModule)
        add(MplCommonNetworkModule)
        add(MplCommonPersistenceModule)
        add(MplCommonFirebaseModule)
        add(MplFeedModule)
        add(MplFeedDataModule)
        add(MplPostModule)
        add(MplPostDataModule)
    }
    register(modules)
}

/** Helper for injecting a [FeedViewModel] */
@Suppress("unused")
fun DependencyInjector.injectFeedViewModel(): FeedViewModel = inject()

/** Helper for injecting a [PostViewModel] */
@Suppress("unused")
fun DependencyInjector.injectPostViewModel(postId: String): PostViewModel = inject(postId)
