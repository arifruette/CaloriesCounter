package ru.ari.caloriescounter.feature.diary.data.remote.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.ari.caloriescounter.feature.diary.data.remote.dto.OpenFoodFactsProductResponse
import ru.ari.caloriescounter.feature.diary.data.remote.dto.OpenFoodFactsSearchResponse

interface OpenFoodFactsApi {

    @GET("cgi/search.pl")
    suspend fun searchByName(
        @Query("search_terms") query: String,
        @Query("search_simple") searchSimple: Int = 1,
        @Query("action") action: String = "process",
        @Query("json") json: Int = 1,
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
