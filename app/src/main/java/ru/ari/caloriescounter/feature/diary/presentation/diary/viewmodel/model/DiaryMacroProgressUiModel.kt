package ru.ari.caloriescounter.feature.diary.presentation.diary.viewmodel.model

data class DiaryMacroProgressUiModel(
    val current: Double,
    val goal: Double,
) {
    val progress: Float = if (goal <= 0.0) 0f else (current / goal).coerceIn(0.0, 1.0).toFloat()
}
