package ru.ari.caloriescounter.feature.diary.presentation.diary.viewmodel.contract

import ru.ari.caloriescounter.core.common.mvi.contracts.UiState
import ru.ari.caloriescounter.feature.diary.domain.model.meal.MealType
import ru.ari.caloriescounter.feature.diary.presentation.diary.viewmodel.model.MealCardUiModel
import ru.ari.caloriescounter.feature.diary.presentation.diary.viewmodel.model.WeightCardUiModel

data class DiaryState(
    val weightCard: WeightCardUiModel = WeightCardUiModel(),
    val mealCards: List<MealCardUiModel> = MealType.entries.map { mealType ->
        MealCardUiModel(mealType = mealType)
    },
    val isLoading: Boolean = true,
) : UiState
