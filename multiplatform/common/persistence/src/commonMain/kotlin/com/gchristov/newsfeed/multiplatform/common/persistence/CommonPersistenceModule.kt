package com.gchristov.newsfeed.multiplatform.common.persistence

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import com.gchristov.newsfeed.multiplatform.common.kotlin.di.DiModule
import com.russhwolf.settings.Settings
import org.kodein.di.DI
import org.kodein.di.bindFactory
import org.kodein.di.bindSingleton

object CommonPersistenceModule : DiModule() {
    override fun name() = "multiplatform-common-persistence"

    override fun bindDependencies(builder: DI.Builder) {
        builder.apply {
            bindFactory { properties: SqlDriverProperties -> provideSqlDriver(properties) }
            bindSingleton { provideSharedPreferences() }
        }
    }
}

internal expect fun provideSqlDriver(properties: SqlDriverProperties): SqlDriver

internal expect fun provideSharedPreferences(): Settings

data class SqlDriverProperties(
    val schema: SqlSchema<QueryResult.Value<Unit>>,
    val databaseName: String,
)