package ru.ari.caloriescounter.feature.diary.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenFoodFactsProductResponse(
    @SerialName("status") val status: Int = 0,
    @SerialName("product") val product: OpenFoodFactsProductDto? = null,
)
