package ru.ari.caloriescounter.feature.diary.domain.model.product

data class ProductRef(
    val source: ProductSource,
    val externalId: String?,
    val barcode: String?,
    val nameRu: String,
    val nameOriginal: String?,
)
