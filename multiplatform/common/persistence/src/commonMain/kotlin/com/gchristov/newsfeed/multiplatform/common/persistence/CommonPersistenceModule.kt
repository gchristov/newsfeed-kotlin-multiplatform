package com.gchristov.newsfeed.multiplatform.common.persistence

import com.gchristov.newsfeed.multiplatform.common.di.DiModule
import com.russhwolf.settings.Settings
import com.squareup.sqldelight.db.SqlDriver
import org.kodein.di.DI
import org.kodein.di.bindFactory
import org.kodein.di.bindSingleton

object CommonPersistenceModule : DiModule() {
    override fun name() = "multiplatform-common-persistence"

    override fun bindLocalDependencies(builder: DI.Builder) {
        builder.apply {
            bindFactory { properties: SqlDriverProperties -> provideSqlDriver(properties) }
            bindSingleton { provideSharedPreferences() }
        }
    }
}

internal expect fun provideSqlDriver(properties: SqlDriverProperties): SqlDriver

internal expect fun provideSharedPreferences(): Settings

data class SqlDriverProperties(
    val schema: SqlDriver.Schema,
    val databaseName: String,
)