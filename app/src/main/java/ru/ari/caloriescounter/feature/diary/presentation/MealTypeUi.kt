package ru.ari.caloriescounter.feature.diary.presentation

import androidx.annotation.StringRes
import java.util.Locale
import ru.ari.caloriescounter.R
import ru.ari.caloriescounter.feature.diary.domain.model.MealType

@StringRes
fun MealType.titleRes(): Int = when (this) {
    MealType.BREAKFAST -> R.string.meal_breakfast
    MealType.LUNCH -> R.string.meal_lunch
    MealType.SNACK -> R.string.meal_snack
}

fun Double.formatRuDecimal(): String = String.format(Locale.forLanguageTag("ru-RU"), "%.1f", this)
