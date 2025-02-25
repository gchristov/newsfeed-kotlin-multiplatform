package com.gchristov.newsfeed.multiplatform.common.firebase

import com.gchristov.newsfeed.multiplatform.common.kotlin.AppContext
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseApp
import dev.gitlive.firebase.initialize

internal actual fun provideFirebaseApp(): FirebaseApp =
    requireNotNull(Firebase.initialize(context = AppContext))