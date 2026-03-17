package ru.ari.caloriescounter.core.common.time

import java.time.Duration
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoscowDateTimeProvider @Inject constructor() {
    private val zoneId: ZoneId = ZoneId.of("Europe/Moscow")

    fun currentDate(): LocalDate = LocalDate.now(zoneId)

    fun millisUntilNextMidnight(): Long {
        val now = ZonedDateTime.now(zoneId)
        val nextMidnight = now.toLocalDate().plusDays(1).atStartOfDay(zoneId)
        return Duration.between(now, nextMidnight).toMillis().coerceAtLeast(1L)
    }
}
