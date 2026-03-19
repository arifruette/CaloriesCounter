package ru.ari.caloriescounter.feature.stats.data

import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import ru.ari.caloriescounter.core.database.dao.CalorieEntryDao
import ru.ari.caloriescounter.core.database.dao.NutritionGoalsDao
import ru.ari.caloriescounter.core.database.entity.CalorieEntryEntity
import ru.ari.caloriescounter.feature.diary.domain.model.meal.MealType
import ru.ari.caloriescounter.feature.diary.domain.model.nutrition.NutritionGoals
import ru.ari.caloriescounter.feature.stats.domain.StatsRepository
import ru.ari.caloriescounter.feature.stats.domain.model.DailyCaloriePoint
import ru.ari.caloriescounter.feature.stats.domain.model.WeeklySummary

@Singleton
class StatsRepositoryImpl @Inject constructor(
    private val calorieEntryDao: CalorieEntryDao,
    private val nutritionGoalsDao: NutritionGoalsDao,
) : StatsRepository {

    override fun observeWeeklySummary(endDate: LocalDate): Flow<WeeklySummary> {
        val startDate = endDate.minusDays(WINDOW_DAYS - 1)
        return combine(
            calorieEntryDao.observeEntriesBetween(startDate = startDate.toString(), endDate = endDate.toString()),
            nutritionGoalsDao.observeGoals(),
            calorieEntryDao.observeLoggedDatesAsc(),
        ) { entries, goalsEntity, loggedDates ->
            val goals = goalsEntity?.toDomain() ?: DEFAULT_GOALS
            val dayStats = buildDayStats(startDate = startDate, endDate = endDate, entries = entries)
            val dailyCalories = dayStats.map { (date, day) ->
                DailyCaloriePoint(
                    date = date,
                    calories = day.calories,
                    isGoalCompleted = day.isGoalCompleted(goals),
                )
            }
            val goalCompletedDays = dailyCalories.count { it.isGoalCompleted }
            val loggedDateSet = loggedDates.mapNotNullTo(mutableSetOf()) { parseLocalDateOrNull(it) }

            WeeklySummary(
                startDate = startDate,
                endDate = endDate,
                dailyCalories = dailyCalories,
                averageCaloriesPerDay = dayStats.values.sumOf { it.calories } / WINDOW_DAYS.toDouble(),
                goalCompletedDays = goalCompletedDays,
                averageProteinPerDay = dayStats.values.sumOf { it.protein } / WINDOW_DAYS.toDouble(),
                averageMealsPerDay = dayStats.values.sumOf { it.uniqueMealsCount.toDouble() } / WINDOW_DAYS.toDouble(),
                bestStreakDays = calculateBestStreak(loggedDateSet),
                currentStreakDays = calculateCurrentStreak(loggedDateSet, endDate = endDate),
                loggedDaysInWindow = dayStats.values.count { it.hasEntries },
            )
        }
    }
}

private data class DayStats(
    val calories: Double = 0.0,
    val protein: Double = 0.0,
    val fat: Double = 0.0,
    val carbs: Double = 0.0,
    val meals: Set<MealType> = emptySet(),
    val hasEntries: Boolean = false,
) {
    val uniqueMealsCount: Int get() = meals.size
}

private fun buildDayStats(
    startDate: LocalDate,
    endDate: LocalDate,
    entries: List<CalorieEntryEntity>,
): LinkedHashMap<LocalDate, DayStats> {
    val initial = linkedMapOf<LocalDate, DayStats>()
    var currentDate = startDate
    while (!currentDate.isAfter(endDate)) {
        initial[currentDate] = DayStats()
        currentDate = currentDate.plusDays(1)
    }

    entries.forEach { entity ->
        val date = parseLocalDateOrNull(entity.dayDate) ?: return@forEach
        val current = initial[date] ?: return@forEach
        val mealType = runCatching { MealType.valueOf(entity.mealType) }.getOrElse { MealType.SNACK }
        val multiplier = entity.portionGrams / 100.0
        val calories = entity.caloriesPer100g * multiplier
        val protein = entity.proteinPer100g * multiplier
        val fat = entity.fatPer100g * multiplier
        val carbs = entity.carbsPer100g * multiplier

        initial[date] = current.copy(
            calories = current.calories + calories,
            protein = current.protein + protein,
            fat = current.fat + fat,
            carbs = current.carbs + carbs,
            meals = current.meals + mealType,
            hasEntries = true,
        )
    }

    return initial
}

private fun DayStats.isGoalCompleted(goals: NutritionGoals): Boolean =
    calories >= goals.calories &&
        protein >= goals.protein &&
        fat >= goals.fat &&
        carbs >= goals.carbs

private fun calculateBestStreak(loggedDates: Set<LocalDate>): Int {
    if (loggedDates.isEmpty()) return 0
    val sortedDates = loggedDates.sorted()
    var maxStreak = 1
    var currentStreak = 1
    for (index in 1 until sortedDates.size) {
        if (sortedDates[index - 1].plusDays(1) == sortedDates[index]) {
            currentStreak += 1
            if (currentStreak > maxStreak) {
                maxStreak = currentStreak
            }
        } else {
            currentStreak = 1
        }
    }
    return maxStreak
}

private fun calculateCurrentStreak(loggedDates: Set<LocalDate>, endDate: LocalDate): Int {
    if (!loggedDates.contains(endDate)) return 0
    var streak = 0
    var cursor = endDate
    while (loggedDates.contains(cursor)) {
        streak += 1
        cursor = cursor.minusDays(1)
    }
    return streak
}

private fun parseLocalDateOrNull(value: String): LocalDate? =
    runCatching { LocalDate.parse(value) }.getOrNull()

private fun ru.ari.caloriescounter.core.database.entity.NutritionGoalsEntity.toDomain(): NutritionGoals =
    NutritionGoals(
        calories = calories,
        protein = protein,
        fat = fat,
        carbs = carbs,
    )

private const val WINDOW_DAYS = 7L

private val DEFAULT_GOALS = NutritionGoals(
    calories = 2000,
    protein = 100.0,
    fat = 70.0,
    carbs = 250.0,
)
