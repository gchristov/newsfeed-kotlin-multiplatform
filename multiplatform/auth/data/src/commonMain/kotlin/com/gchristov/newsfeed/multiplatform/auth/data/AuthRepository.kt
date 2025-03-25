package com.gchristov.newsfeed.multiplatform.auth.data

import arrow.core.Either
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface AuthRepository {
    suspend fun getUserSession(): Either<Throwable, UserSession>
}

internal class RealAuthRepository(
    private val dispatcher: CoroutineDispatcher,
    database: AuthSqlDelightDatabase,
) : AuthRepository {
    private val queries = database.authSqlDelightDatabaseQueries

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun getUserSession(): Either<Throwable, UserSession> =
        withContext(dispatcher) {
            try {
                val userSession: UserSession
                val userSessionRes = queries.getSession().executeAsOneOrNull()
                if (userSessionRes == null) {
                    userSession = UserSession(
                        id = Uuid.random().toString(),
                        userName = "User Name",
                    )
                    queries.transaction {
                        queries.insertSession(
                            id = userSession.id,
                            userName = userSession.userName,
                        )
                    }
                } else {
                    userSession = UserSession(
                        id = userSessionRes.id,
                        userName = userSessionRes.userName
                    )
                }
                Either.Right(userSession)
            } catch (error: Throwable) {
                Either.Left(error)
            }
        }
}