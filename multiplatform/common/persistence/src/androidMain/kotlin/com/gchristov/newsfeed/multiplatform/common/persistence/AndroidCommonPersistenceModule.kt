package com.gchristov.newsfeed.multiplatform.common.persistence

import com.gchristov.newsfeed.multiplatform.common.di.AppContext
import com.russhwolf.settings.AndroidSettings
import com.russhwolf.settings.Settings
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

internal actual fun provideSqlDriver(properties: SqlDriverProperties): SqlDriver =
    AndroidSqliteDriver(
        context = AppContext,
        schema = properties.schema,
        name = properties.databaseName
    )

internal actual fun provideSharedPreferences(): Settings =
    AndroidSettings.Factory(AppContext).create()