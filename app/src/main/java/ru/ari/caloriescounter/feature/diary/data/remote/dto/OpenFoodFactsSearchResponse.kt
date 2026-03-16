package ru.ari.caloriescounter.feature.diary.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenFoodFactsSearchResponse(
    @SerialName("products") val products: List<OpenFoodFactsProductDto> = emptyList(),
)
