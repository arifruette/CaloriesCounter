package ru.ari.caloriescounter.feature.diary.presentation.common

import androidx.annotation.StringRes
import ru.ari.caloriescounter.R
import ru.ari.caloriescounter.feature.diary.domain.model.meal.MealType

@StringRes
fun MealType.titleRes(): Int = when (this) {
    MealType.BREAKFAST -> R.string.meal_breakfast
    MealType.LUNCH -> R.string.meal_lunch
    MealType.DINNER -> R.string.meal_dinner
    MealType.SNACK -> R.string.meal_snack
}
