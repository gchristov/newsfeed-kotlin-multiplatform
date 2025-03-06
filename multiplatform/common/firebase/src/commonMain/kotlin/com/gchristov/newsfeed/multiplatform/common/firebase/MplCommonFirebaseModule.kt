package com.gchristov.newsfeed.multiplatform.common.firebase

import com.gchristov.newsfeed.multiplatform.common.kotlin.di.DependencyModule
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseApp
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.firestore
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

object MplCommonFirebaseModule : DependencyModule() {
    override fun name() = "mpl-common-firebase"

    override fun bindDependencies(builder: DI.Builder) {
        builder.apply {
            bindSingleton { provideFirebaseApp() }
            bindSingleton { provideFirestore(app = instance()) }
        }
    }

    private fun provideFirestore(app: FirebaseApp): FirebaseFirestore = Firebase.firestore(app)
}

internal expect fun provideFirebaseApp(): FirebaseApp
