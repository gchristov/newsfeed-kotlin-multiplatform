package com.gchristov.newsfeed.kmmcommonpersistence

import com.gchristov.newsfeed.kmmcommondi.DiModule
import com.russhwolf.settings.Settings
import com.squareup.sqldelight.db.SqlDriver
import org.kodein.di.DI
import org.kodein.di.bindFactory
import org.kodein.di.bindSingleton

object CommonPersistenceModule : DiModule() {
    override fun name() = "kmm-common-persistence"

    override fun build(builder: DI.Builder) {
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