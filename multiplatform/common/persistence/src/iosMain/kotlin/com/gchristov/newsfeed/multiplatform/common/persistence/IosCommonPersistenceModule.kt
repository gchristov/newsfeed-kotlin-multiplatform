package com.gchristov.newsfeed.multiplatform.common.persistence

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings

internal actual fun provideSqlDriver(properties: SqlDriverProperties): SqlDriver =
    NativeSqliteDriver(
        schema = properties.schema,
        name = properties.databaseName
    )

internal actual fun provideSharedPreferences(): Settings = NSUserDefaultsSettings.Factory().create()