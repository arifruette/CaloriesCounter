package ru.ari.caloriescounter.feature.stats.domain.interactor

import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import ru.ari.caloriescounter.feature.stats.domain.StatsRepository
import ru.ari.caloriescounter.feature.stats.domain.model.WeeklySummary

class StatsInteractorImpl @Inject constructor(
    private val repository: StatsRepository,
) : StatsInteractor {
    override fun observeWeeklySummary(endDate: LocalDate): Flow<WeeklySummary> =
        repository.observeWeeklySummary(endDate)
}
