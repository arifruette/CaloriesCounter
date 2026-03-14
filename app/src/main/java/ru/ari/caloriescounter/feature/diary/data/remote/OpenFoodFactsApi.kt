package ru.ari.caloriescounter.feature.diary.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenFoodFactsApi {

    @GET("api/v2/search")
    suspend fun searchByName(
        @Query("search_terms") query: String,
        @Query("page_size") pageSize: Int = 20,
        @Query("fields") fields: String = OPEN_FOOD_FACTS_FIELDS,
    ): OpenFoodFactsSearchResponse

    @GET("api/v2/product/{barcode}.json")
    suspend fun productByBarcode(
        @Path("barcode") barcode: String,
        @Query("fields") fields: String = OPEN_FOOD_FACTS_FIELDS,
    ): OpenFoodFactsProductResponse
}

private const val OPEN_FOOD_FACTS_FIELDS =
    "code,product_name,product_name_ru,nutriments"

@Serializable
data class OpenFoodFactsSearchResponse(
    @SerialName("products") val products: List<OpenFoodFactsProductDto> = emptyList(),
)

@Serializable
data class OpenFoodFactsProductResponse(
    @SerialName("status") val status: Int = 0,
    @SerialName("product") val product: OpenFoodFactsProductDto? = null,
)

@Serializable
data class OpenFoodFactsProductDto(
    @SerialName("code") val code: String? = null,
    @SerialName("product_name") val productName: String? = null,
    @SerialName("product_name_ru") val productNameRu: String? = null,
    @SerialName("nutriments") val nutriments: OpenFoodFactsNutrimentsDto? = null,
)

@Serializable
data class OpenFoodFactsNutrimentsDto(
    @SerialName("energy-kcal_100g") val energyKcal100g: Double? = null,
    @SerialName("proteins_100g") val proteins100g: Double? = null,
    @SerialName("fat_100g") val fat100g: Double? = null,
    @SerialName("carbohydrates_100g") val carbohydrates100g: Double? = null,
)
