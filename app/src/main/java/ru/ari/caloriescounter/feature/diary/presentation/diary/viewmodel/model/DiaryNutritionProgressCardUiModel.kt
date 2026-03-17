package ru.ari.caloriescounter.feature.diary.presentation.diary.viewmodel.model

data class DiaryNutritionProgressCardUiModel(
    val calories: DiaryMacroProgressUiModel = DiaryMacroProgressUiModel(0.0, 2000.0),
    val protein: DiaryMacroProgressUiModel = DiaryMacroProgressUiModel(0.0, 100.0),
    val fat: DiaryMacroProgressUiModel = DiaryMacroProgressUiModel(0.0, 70.0),
    val carbs: DiaryMacroProgressUiModel = DiaryMacroProgressUiModel(0.0, 250.0),
)
