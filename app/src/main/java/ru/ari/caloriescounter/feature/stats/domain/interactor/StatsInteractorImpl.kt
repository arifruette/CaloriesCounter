package ru.ari.caloriescounter.feature.stats.domain.interactor

import javax.inject.Inject
import ru.ari.caloriescounter.feature.stats.domain.StatsRepository

class StatsInteractorImpl @Inject constructor(
    private val repository: StatsRepository,
) : StatsInteractor
