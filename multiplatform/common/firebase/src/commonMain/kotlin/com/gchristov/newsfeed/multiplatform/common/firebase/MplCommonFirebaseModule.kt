package com.gchristov.newsfeed.multiplatform.common.firebase

import co.touchlab.crashkios.crashlytics.enableCrashlytics
import co.touchlab.kermit.ExperimentalKermitApi
import co.touchlab.kermit.Logger
import co.touchlab.kermit.crashlytics.CrashlyticsLogWriter
import com.gchristov.newsfeed.multiplatform.common.kotlin.di.DependencyModule
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseApp
import dev.gitlive.firebase.analytics.FirebaseAnalytics
import dev.gitlive.firebase.analytics.analytics
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.firestore
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

object MplCommonFirebaseModule : DependencyModule() {
    override fun name() = "mpl-common-firebase"

    @OptIn(ExperimentalKermitApi::class)
    override fun bindDependencies(builder: DI.Builder) {
        // Enable Kermit for Crashlytics
        Logger.addLogWriter(CrashlyticsLogWriter())
        // Enable CrashKiOS
        enableCrashlytics()

        builder.apply {
            bindSingleton { provideFirebaseApp() }
            bindSingleton { provideFirestore(app = instance()) }
            bindSingleton { provideAnalytics(app = instance()) }
        }
    }

    private fun provideFirestore(app: FirebaseApp): FirebaseFirestore = Firebase.firestore(app)

    private fun provideAnalytics(app: FirebaseApp): FirebaseAnalytics = Firebase.analytics(app)
}

internal expect fun provideFirebaseApp(): FirebaseApp
