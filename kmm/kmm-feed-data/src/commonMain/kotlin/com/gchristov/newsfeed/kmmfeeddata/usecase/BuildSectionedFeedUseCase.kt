package com.gchristov.newsfeed.kmmfeeddata.usecase

import com.gchristov.newsfeed.kmmfeeddata.model.DecoratedFeedItem
import com.gchristov.newsfeed.kmmfeeddata.model.DecoratedFeedPage
import com.gchristov.newsfeed.kmmfeeddata.model.SectionedFeed
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.datetime.*

class BuildSectionedFeedUseCase(
    private val dispatcher: CoroutineDispatcher,
    private val clock: Clock,
) {
    suspend operator fun invoke(feed: DecoratedFeedPage): SectionedFeed =
        withContext(dispatcher) {
            val sectionsMap = feed.items
                .sortedByDescending { it.date.toEpochMilliseconds() }
                .groupBy { it.sectionType(clock) }
            val sections = sectionsMap.keys.map {
                SectionedFeed.Section(
                    type = it,
                    feedItems = requireNotNull(sectionsMap[it])
                )
            }
            SectionedFeed(
                pages = feed.raw.pages,
                currentPage = feed.raw.pageId,
                sections = sections
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