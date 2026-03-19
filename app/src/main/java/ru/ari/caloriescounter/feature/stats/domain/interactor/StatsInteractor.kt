package ru.ari.caloriescounter.feature.stats.domain.interactor

import java.time.LocalDate
import kotlinx.coroutines.flow.Flow
import ru.ari.caloriescounter.feature.stats.domain.model.WeeklySummary

interface StatsInteractor {
    fun observeWeeklySummary(endDate: LocalDate): Flow<WeeklySummary>
}
