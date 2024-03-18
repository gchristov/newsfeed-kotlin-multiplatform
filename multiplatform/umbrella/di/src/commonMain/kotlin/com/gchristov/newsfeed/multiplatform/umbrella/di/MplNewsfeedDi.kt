package com.gchristov.newsfeed.multiplatform.umbrella.di

import com.gchristov.newsfeed.multiplatform.common.kotlin.CommonKotlinModule
import com.gchristov.newsfeed.multiplatform.common.kotlin.di.DiGraph
import com.gchristov.newsfeed.multiplatform.common.kotlin.di.registerModules
import com.gchristov.newsfeed.multiplatform.common.network.CommonNetworkModule
import com.gchristov.newsfeed.multiplatform.common.persistence.CommonPersistenceModule
import com.gchristov.newsfeed.multiplatform.feed.data.FeedDataModule
import com.gchristov.newsfeed.multiplatform.feed.feature.FeedModule
import com.gchristov.newsfeed.multiplatform.post.data.PostDataModule
import com.gchristov.newsfeed.multiplatform.post.feature.PostModule
import org.kodein.di.DI

object MplNewsfeedDi {
    fun setup(appModules: List<DI.Module>) {
        val dependencies = mutableListOf<DI.Module>().apply {
            add(CommonKotlinModule.module)
            add(CommonNetworkModule.module)
            add(CommonPersistenceModule.module)
            add(FeedModule.module)
            add(FeedDataModule.module)
            add(PostModule.module)
            add(PostDataModule.module)
            addAll(appModules)
        }
        DiGraph.registerModules(dependencies)
    }
}