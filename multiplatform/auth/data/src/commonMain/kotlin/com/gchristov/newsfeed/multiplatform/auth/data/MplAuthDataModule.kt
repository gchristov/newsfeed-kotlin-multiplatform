package com.gchristov.newsfeed.multiplatform.auth.data

import com.gchristov.newsfeed.multiplatform.common.kotlin.di.DependencyModule
import com.gchristov.newsfeed.multiplatform.common.persistence.SqlDriverProperties
import kotlinx.coroutines.Dispatchers
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

object MplAuthDataModule : DependencyModule() {
    override fun name() = "mpl-auth-data"

    override fun bindDependencies(builder: DI.Builder) {
        builder.apply {
            bindSingleton {
                provideAuthRepository(
                    database = AuthSqlDelightDatabase(
                        instance(
                            arg = SqlDriverProperties(
                                schema = AuthSqlDelightDatabase.Schema,
                                databaseName = "auth.db"
                            )
                        )
                    ),
                )
            }
        }
    }

    private fun provideAuthRepository(
        database: AuthSqlDelightDatabase,
    ): AuthRepository = RealAuthRepository(
        dispatcher = Dispatchers.Default,
        database = database,
    )
}
