package ru.ari.caloriescounter.feature.stats.domain

import javax.inject.Inject

interface StatsInteractor

class StatsInteractorImpl @Inject constructor(
    private val repository: StatsRepository,
) : StatsInteractor
