package ru.ari.caloriescounter.feature.diary.presentation.navigation

import ru.ari.caloriescounter.feature.diary.domain.model.MealType

sealed interface DiaryNavTarget {
    data class MealProducts(val mealType: MealType) : DiaryNavTarget
}
