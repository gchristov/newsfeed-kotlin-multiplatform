package com.gchristov.newsfeed.multiplatform.common.firebase

import co.touchlab.crashkios.crashlytics.setCrashlyticsUnhandledExceptionHook
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseApp
import dev.gitlive.firebase.initialize

internal actual fun provideFirebaseApp(): FirebaseApp {
    // Allows catching unhandled exceptions on iOS
    setCrashlyticsUnhandledExceptionHook()
    return requireNotNull(Firebase.initialize())
}