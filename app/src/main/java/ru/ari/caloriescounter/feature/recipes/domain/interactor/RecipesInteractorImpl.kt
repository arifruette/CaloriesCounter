package ru.ari.caloriescounter.feature.recipes.domain.interactor

import javax.inject.Inject
import ru.ari.caloriescounter.feature.recipes.domain.RecipesRepository

class RecipesInteractorImpl @Inject constructor(
    private val repository: RecipesRepository,
) : RecipesInteractor
