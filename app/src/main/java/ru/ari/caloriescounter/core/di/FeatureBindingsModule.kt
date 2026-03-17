package ru.ari.caloriescounter.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import ru.ari.caloriescounter.feature.diary.data.DiaryRepositoryImpl
import ru.ari.caloriescounter.feature.diary.data.ProductSearchRepositoryImpl
import ru.ari.caloriescounter.feature.diary.data.WeightProfileRepositoryImpl
import ru.ari.caloriescounter.feature.diary.domain.DiaryRepository
import ru.ari.caloriescounter.feature.diary.domain.ProductSearchRepository
import ru.ari.caloriescounter.feature.diary.domain.WeightProfileRepository
import ru.ari.caloriescounter.feature.diary.domain.interactor.DiaryInteractor
import ru.ari.caloriescounter.feature.diary.domain.interactor.DiaryInteractorImpl
import ru.ari.caloriescounter.feature.diary.domain.interactor.ProductSearchInteractor
import ru.ari.caloriescounter.feature.diary.domain.interactor.ProductSearchInteractorImpl
import ru.ari.caloriescounter.feature.diary.domain.interactor.WeightProfileInteractor
import ru.ari.caloriescounter.feature.diary.domain.interactor.WeightProfileInteractorImpl
import ru.ari.caloriescounter.feature.recipes.data.RecipesRepositoryImpl
import ru.ari.caloriescounter.feature.recipes.domain.RecipesRepository
import ru.ari.caloriescounter.feature.recipes.domain.interactor.RecipesInteractor
import ru.ari.caloriescounter.feature.recipes.domain.interactor.RecipesInteractorImpl
import ru.ari.caloriescounter.feature.stats.data.StatsRepositoryImpl
import ru.ari.caloriescounter.feature.stats.domain.StatsRepository
import ru.ari.caloriescounter.feature.stats.domain.interactor.StatsInteractor
import ru.ari.caloriescounter.feature.stats.domain.interactor.StatsInteractorImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class FeatureBindingsModule {

    @Binds
    @Singleton
    abstract fun bindDiaryRepository(impl: DiaryRepositoryImpl): DiaryRepository

    @Binds
    @Singleton
    abstract fun bindStatsRepository(impl: StatsRepositoryImpl): StatsRepository

    @Binds
    @Singleton
    abstract fun bindWeightProfileRepository(impl: WeightProfileRepositoryImpl): WeightProfileRepository

    @Binds
    @Singleton
    abstract fun bindProductSearchRepository(impl: ProductSearchRepositoryImpl): ProductSearchRepository

    @Binds
    @Singleton
    abstract fun bindRecipesRepository(impl: RecipesRepositoryImpl): RecipesRepository

    @Binds
    @Singleton
    abstract fun bindDiaryInteractor(impl: DiaryInteractorImpl): DiaryInteractor

    @Binds
    @Singleton
    abstract fun bindStatsInteractor(impl: StatsInteractorImpl): StatsInteractor

    @Binds
    @Singleton
    abstract fun bindWeightProfileInteractor(impl: WeightProfileInteractorImpl): WeightProfileInteractor

    @Binds
    @Singleton
    abstract fun bindProductSearchInteractor(impl: ProductSearchInteractorImpl): ProductSearchInteractor

    @Binds
    @Singleton
    abstract fun bindRecipesInteractor(impl: RecipesInteractorImpl): RecipesInteractor
}

