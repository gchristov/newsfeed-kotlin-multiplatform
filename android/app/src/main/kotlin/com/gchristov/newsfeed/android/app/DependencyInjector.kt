package com.gchristov.newsfeed.android.app

import android.content.Context
import com.gchristov.newsfeed.android.common.navigation.NavigationModule
import com.gchristov.newsfeed.multiplatform.common.firebase.MplCommonFirebaseModule
import com.gchristov.newsfeed.multiplatform.common.kotlin.AppContext
import com.gchristov.newsfeed.multiplatform.common.kotlin.MplCommonKotlinModule
import com.gchristov.newsfeed.multiplatform.common.kotlin.di.DependencyInjector
import com.gchristov.newsfeed.multiplatform.common.kotlin.di.DependencyModule
import com.gchristov.newsfeed.multiplatform.common.kotlin.di.register
import com.gchristov.newsfeed.multiplatform.common.network.MplCommonNetworkModule
import com.gchristov.newsfeed.multiplatform.common.persistence.MplCommonPersistenceModule
import com.gchristov.newsfeed.multiplatform.feed.data.MplFeedDataModule
import com.gchristov.newsfeed.multiplatform.feed.feature.MplFeedModule
import com.gchristov.newsfeed.multiplatform.post.data.MplPostDataModule
import com.gchristov.newsfeed.multiplatform.post.feature.MplPostModule

/**
 * Initialises the dependency graph for Android.
 *
 * When making changes, consider also updating the multiplatform/umbrella module.
 */

internal fun DependencyInjector.initialise(context: Context) {
    AppContext = context
    val modules = mutableListOf<DependencyModule>().apply {
        // Multiplatform modules
        add(MplCommonKotlinModule)
        add(MplCommonNetworkModule)
        add(MplCommonPersistenceModule)
        add(MplCommonFirebaseModule)
        add(MplFeedModule)
        add(MplFeedDataModule)
        add(MplPostModule)
        add(MplPostDataModule)
        // Android modules
        add(NavigationModule)
    }
    register(modules)
}