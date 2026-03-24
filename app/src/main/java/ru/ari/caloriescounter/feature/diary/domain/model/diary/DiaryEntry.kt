package ru.ari.caloriescounter.feature.diary.domain.model.diary

import java.time.LocalDate
import kotlin.math.round
import ru.ari.caloriescounter.feature.diary.domain.model.nutrition.NutritionPer100g
import ru.ari.caloriescounter.feature.diary.domain.model.nutrition.Portion
import ru.ari.caloriescounter.feature.diary.domain.model.product.ProductRef

data class DiaryEntry(
    val id: Long,
    val date: LocalDate,
    val mealKey: String,
    val product: ProductRef,
    val nutritionPer100g: NutritionPer100g,
    val portion: Portion,
) {
    fun totalCalories(): Double = roundToOneDecimal(nutritionPer100g.calories * portion.grams / 100.0)
    fun totalProtein(): Double = roundToOneDecimal(nutritionPer100g.protein * portion.grams / 100.0)
    fun totalFat(): Double = roundToOneDecimal(nutritionPer100g.fat * portion.grams / 100.0)
    fun totalCarbs(): Double = roundToOneDecimal(nutritionPer100g.carbs * portion.grams / 100.0)

    private fun roundToOneDecimal(value: Double): Double = round(value * 10.0) / 10.0
}
