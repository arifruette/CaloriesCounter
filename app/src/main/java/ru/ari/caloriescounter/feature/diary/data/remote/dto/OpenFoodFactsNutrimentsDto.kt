package ru.ari.caloriescounter.feature.diary.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenFoodFactsNutrimentsDto(
    @SerialName("energy-kcal_100g") val energyKcal100g: Double? = null,
    @SerialName("proteins_100g") val proteins100g: Double? = null,
    @SerialName("fat_100g") val fat100g: Double? = null,
    @SerialName("carbohydrates_100g") val carbohydrates100g: Double? = null,
)
