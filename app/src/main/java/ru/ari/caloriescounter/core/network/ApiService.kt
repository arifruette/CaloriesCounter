package ru.ari.caloriescounter.core.network

import retrofit2.http.GET
import ru.ari.caloriescounter.core.network.dto.HealthResponse

interface ApiService {
    @GET("v1/health")
    suspend fun health(): HealthResponse
}
