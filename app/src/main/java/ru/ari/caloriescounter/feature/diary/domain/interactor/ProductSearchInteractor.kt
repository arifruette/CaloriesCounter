package ru.ari.caloriescounter.feature.diary.domain.interactor

import ru.ari.caloriescounter.feature.diary.domain.model.product.ProductCandidate

interface ProductSearchInteractor {
    suspend fun searchByName(query: String): List<ProductCandidate>
}
