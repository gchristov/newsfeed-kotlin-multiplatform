package com.gchristov.newsfeed.multiplatform.common.persistence

import com.russhwolf.settings.AppleSettings
import com.russhwolf.settings.Settings
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

internal actual fun provideSqlDriver(properties: SqlDriverProperties): SqlDriver =
    NativeSqliteDriver(
        schema = properties.schema,
        name = properties.databaseName
    )

internal actual fun provideSharedPreferences(): Settings = AppleSettings.Factory().create()