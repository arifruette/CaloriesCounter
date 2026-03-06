package ru.ari.caloriescounter.core.network

import kotlinx.serialization.Serializable
import retrofit2.http.GET

interface ApiService {
    @GET("v1/health")
    suspend fun health(): HealthResponse
}

@Serializable
data class HealthResponse(
    val status: String,
)
