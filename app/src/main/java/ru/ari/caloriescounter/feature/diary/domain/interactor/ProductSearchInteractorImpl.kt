package ru.ari.caloriescounter.feature.diary.domain.interactor

import javax.inject.Inject
import ru.ari.caloriescounter.feature.diary.domain.ProductSearchRepository
import ru.ari.caloriescounter.feature.diary.domain.model.product.ProductCandidate

class ProductSearchInteractorImpl @Inject constructor(
    private val repository: ProductSearchRepository,
) : ProductSearchInteractor {
    override suspend fun searchByName(query: String): List<ProductCandidate> = repository.searchByName(query)
}
