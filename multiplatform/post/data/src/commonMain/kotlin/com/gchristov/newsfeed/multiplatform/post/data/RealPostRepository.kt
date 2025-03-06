package com.gchristov.newsfeed.multiplatform.post.data

import arrow.core.Either
import arrow.core.raise.either
import com.gchristov.newsfeed.multiplatform.common.kotlin.di.DependencyInjector
import com.gchristov.newsfeed.multiplatform.common.kotlin.di.inject
import com.gchristov.newsfeed.multiplatform.post.data.api.ApiPostResponse
import com.gchristov.newsfeed.multiplatform.post.data.model.DecoratedPost
import com.gchristov.newsfeed.multiplatform.post.data.model.toPost
import com.gchristov.newsfeed.multiplatform.post.data.usecase.EstimateReadingTimeMinutesUseCase
import com.russhwolf.settings.Settings
import com.russhwolf.settings.contains
import com.russhwolf.settings.set
import dev.gitlive.firebase.firestore.FirebaseFirestore
import io.ktor.client.call.body
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

internal class RealPostRepository(
    private val dispatcher: CoroutineDispatcher,
    private val apiService: PostApi,
    private val sharedPreferences: Settings,
    private val estimateReadingTimeMinutesUseCase: EstimateReadingTimeMinutesUseCase,
    database: PostSqlDelightDatabase
) : PostRepository {
    private val queries = database.postSqlDelightDatabaseQueries

    // TODO: Remove eventually
    private val firestore: FirebaseFirestore = DependencyInjector.inject()

    override suspend fun post(
        postId: String,
        postMetadataFields: String
    ): Either<Throwable, DecoratedPost> = withContext(dispatcher) {
        either {
            val postRsp = apiService.post(
                postUrl = postId,
                postMetadataFields = postMetadataFields,
            ).bind().body<ApiPostResponse>()
            val post = decoratePost(postRsp.toPost()).bind()
            clearCache(post.raw.id).bind()
            cachePost(post)
            post
        }
    }

    private suspend fun decoratePost(
        post: Post,
    ): Either<Throwable, DecoratedPost> = withContext(dispatcher) {
        either {
            DecoratedPost(
                raw = post,
                date = Instant.parse(post.date),
                favouriteTimestamp = favouriteTimestamp(post.id).bind(),
                readingTimeMinutes = estimateReadingTimeMinutes(post).bind(),
            )
        }
    }

    override suspend fun cachedPost(
        postId: String
    ): Either<Throwable, DecoratedPost?> = withContext(dispatcher) {
        val post = queries.selectWithId(postId).executeAsOneOrNull()
        post?.let {
            decoratePost(post)
        } ?: Either.Right(null)
    }

    override suspend fun clearCache(
        postId: String
    ): Either<Throwable, Unit> = withContext(dispatcher) {
        queries.transaction {
            queries.clearTable(postId)
        }
        Either.Right(Unit)
    }

    override suspend fun cachePost(
        decoratedPost: DecoratedPost
    ): Either<Throwable, Unit> = withContext(dispatcher) {
        val post = decoratedPost.raw
        queries.insert(
            id = post.id,
            date = post.date,
            headline = post.headline,
            body = post.body,
            thumbnail = post.thumbnail,
        )
        Either.Right(Unit)
    }

    override suspend fun favouriteTimestamp(
        postId: String
    ): Either<Throwable, Long?> = withContext(dispatcher) {
        if (sharedPreferences.contains(postId)) {
            val timestamp = sharedPreferences.getLong(
                key = postId,
                defaultValue = Clock.System.now().toEpochMilliseconds()
            )
            Either.Right(timestamp)
        } else {
            Either.Right(null)
        }
    }

    override suspend fun toggleFavourite(
        postId: String
    ): Either<Throwable, Unit> = withContext(dispatcher) {
        either {
            println("About to test Firestore")
            val document = firestore.document("preferences/user1").get()
            println("Got Firestore document: exists=${document.exists}, theme=${document.get<String>("theme")}")

            val timestamp = favouriteTimestamp(postId).bind()
            if (timestamp != null) {
                sharedPreferences.remove(postId)
            } else {
                // Keep track of when the item was favourited
                sharedPreferences[postId] = Clock.System.now().toEpochMilliseconds()
            }
            Either.Right(Unit)
        }
    }

    private suspend fun estimateReadingTimeMinutes(
        post: Post
    ): Either<Throwable, Int> = withContext(dispatcher) {
        val allText = post.headline + " " + post.body
        estimateReadingTimeMinutesUseCase(EstimateReadingTimeMinutesUseCase.Dto(allText))
    }
}