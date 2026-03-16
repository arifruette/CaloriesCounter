package ru.ari.caloriescounter.feature.diary.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenFoodFactsProductDto(
    @SerialName("code") val code: String? = null,
    @SerialName("product_name") val productName: String? = null,
    @SerialName("product_name_ru") val productNameRu: String? = null,
    @SerialName("nutriments") val nutriments: OpenFoodFactsNutrimentsDto? = null,
)
