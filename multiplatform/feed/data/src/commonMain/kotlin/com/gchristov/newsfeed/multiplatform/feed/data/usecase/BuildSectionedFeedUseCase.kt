package com.gchristov.newsfeed.multiplatform.feed.data.usecase

import arrow.core.Either
import com.gchristov.newsfeed.multiplatform.feed.data.model.DecoratedFeedItem
import com.gchristov.newsfeed.multiplatform.feed.data.model.DecoratedFeedPage
import com.gchristov.newsfeed.multiplatform.feed.data.model.SectionedFeed
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

interface BuildSectionedFeedUseCase {
    suspend operator fun invoke(feed: DecoratedFeedPage): Either<Throwable, SectionedFeed>
}

class RealBuildSectionedFeedUseCase(
    private val dispatcher: CoroutineDispatcher,
    private val clock: Clock,
) : BuildSectionedFeedUseCase {
    override suspend operator fun invoke(
        feed: DecoratedFeedPage,
    ): Either<Throwable, SectionedFeed> = withContext(dispatcher) {
        val sectionsMap = feed.items
            .sortedByDescending { it.date.toEpochMilliseconds() }
            .groupBy { it.sectionType(clock) }
        val sections = sectionsMap.keys.map {
            SectionedFeed.Section(
                type = it,
                feedItems = requireNotNull(sectionsMap[it])
            )
        }
        Either.Right(
            SectionedFeed(
                pages = feed.raw.pages.toInt(),
                currentPage = feed.raw.pageId.toInt(),
                sections = sections
            )
        )
    }
}

private fun DecoratedFeedItem.sectionType(clock: Clock): SectionedFeed.SectionType = when {
    date.isThisWeek(clock) -> SectionedFeed.SectionType.ThisWeek
    date.isLastWeek(clock) -> SectionedFeed.SectionType.LastWeek
    date.isThisMonth(clock) -> SectionedFeed.SectionType.ThisMonth
    else -> SectionedFeed.SectionType.Older(date.stripDayAndTime())
}

private fun Instant.isThisWeek(clock: Clock): Boolean = isWeeksFromNow(
    clock = clock,
    weeks = -1
)

private fun Instant.isLastWeek(clock: Clock): Boolean = isWeeksFromNow(
    clock = clock,
    weeks = -2
)

private fun Instant.isWeeksFromNow(
    clock: Clock,
    weeks: Int
): Boolean {
    val nowPlusWeeks = clock.now().toLocalDateTime().date.plus(
        weeks,
        DateTimeUnit.WEEK
    )
    return this.toLocalDateTime().date >= nowPlusWeeks
}

private fun Instant.isThisMonth(clock: Clock): Boolean {
    val then = this.toLocalDateTime()
    val now = clock.now().toLocalDateTime()
    return now.year == then.year && now.monthNumber == then.monthNumber
}

private fun Instant.stripDayAndTime(): Instant {
    val then = this.toLocalDateTime()
    val atStartOfMonth = LocalDate(then.year, then.monthNumber, 1)
    return atStartOfMonth.atStartOfDayIn(TimeZone.UTC)
}

fun Instant.toLocalDateTime(): LocalDateTime = toLocalDateTime(TimeZone.UTC)