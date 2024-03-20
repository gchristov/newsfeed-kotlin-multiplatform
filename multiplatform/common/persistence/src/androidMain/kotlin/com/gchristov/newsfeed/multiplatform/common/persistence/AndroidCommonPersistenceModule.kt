package com.gchristov.newsfeed.multiplatform.common.persistence

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.gchristov.newsfeed.multiplatform.common.kotlin.AppContext
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings

internal actual fun provideSqlDriver(properties: SqlDriverProperties): SqlDriver =
    AndroidSqliteDriver(
        context = AppContext,
        schema = properties.schema,
        name = properties.databaseName
    )

internal actual fun provideSharedPreferences(): Settings =
    SharedPreferencesSettings.Factory(AppContext).create()