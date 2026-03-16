package ru.ari.caloriescounter.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class HealthResponse(
    val status: String,
)
