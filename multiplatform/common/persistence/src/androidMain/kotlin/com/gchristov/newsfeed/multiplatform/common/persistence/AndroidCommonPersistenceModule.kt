package com.gchristov.newsfeed.multiplatform.common.persistence

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.gchristov.newsfeed.multiplatform.common.kotlin.AppContext
import com.russhwolf.settings.AndroidSettings
import com.russhwolf.settings.Settings

internal actual fun provideSqlDriver(properties: SqlDriverProperties): SqlDriver =
    AndroidSqliteDriver(
        context = AppContext,
        schema = properties.schema,
        name = properties.databaseName
    )

internal actual fun provideSharedPreferences(): Settings =
    AndroidSettings.Factory(AppContext).create()