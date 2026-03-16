package ru.ari.caloriescounter.feature.diary.domain

import ru.ari.caloriescounter.feature.diary.domain.model.product.ProductCandidate

interface ProductSearchRepository {
    suspend fun searchByName(query: String): List<ProductCandidate>
    suspend fun searchByBarcode(barcode: String): ProductCandidate?
}

