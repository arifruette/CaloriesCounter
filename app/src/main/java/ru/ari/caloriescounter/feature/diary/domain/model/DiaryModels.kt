package ru.ari.caloriescounter.feature.diary.domain.model

import java.time.LocalDate
import kotlin.math.round

data class DiaryEntry(
    val id: Long,
    val date: LocalDate,
    val mealType: MealType,
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

data class MealSummary(
    val mealType: MealType,
    val entriesCount: Int,
    val totalCalories: Double,
    val totalProtein: Double,
    val totalFat: Double,
    val totalCarbs: Double,
)

data class DayDiary(
    val date: LocalDate,
    val entries: List<DiaryEntry>,
    val mealSummaries: Map<MealType, MealSummary>,
    val totalCalories: Double,
    val totalProtein: Double,
    val totalFat: Double,
    val totalCarbs: Double,
)
