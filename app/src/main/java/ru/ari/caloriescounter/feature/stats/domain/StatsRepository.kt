package ru.ari.caloriescounter.feature.stats.domain

import java.time.LocalDate
import kotlinx.coroutines.flow.Flow
import ru.ari.caloriescounter.feature.stats.domain.model.WeeklySummary

interface StatsRepository {
    fun observeWeeklySummary(endDate: LocalDate): Flow<WeeklySummary>
}
