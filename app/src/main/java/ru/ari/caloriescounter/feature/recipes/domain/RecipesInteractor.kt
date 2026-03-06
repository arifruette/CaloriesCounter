package ru.ari.caloriescounter.feature.recipes.domain

import javax.inject.Inject

interface RecipesInteractor

class RecipesInteractorImpl @Inject constructor(
    private val repository: RecipesRepository,
) : RecipesInteractor
