package ru.ari.caloriescounter.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import ru.ari.caloriescounter.feature.diary.data.DiaryRepositoryImpl
import ru.ari.caloriescounter.feature.diary.data.ProductSearchRepositoryImpl
import ru.ari.caloriescounter.feature.diary.data.WeightProfileRepositoryImpl
import ru.ari.caloriescounter.feature.diary.domain.DiaryInteractor
import ru.ari.caloriescounter.feature.diary.domain.DiaryInteractorImpl
import ru.ari.caloriescounter.feature.diary.domain.DiaryRepository
import ru.ari.caloriescounter.feature.diary.domain.ProductSearchRepository
import ru.ari.caloriescounter.feature.diary.domain.WeightProfileInteractor
import ru.ari.caloriescounter.feature.diary.domain.WeightProfileInteractorImpl
import ru.ari.caloriescounter.feature.diary.domain.WeightProfileRepository
import ru.ari.caloriescounter.feature.recipes.data.RecipesRepositoryImpl
import ru.ari.caloriescounter.feature.recipes.domain.RecipesInteractor
import ru.ari.caloriescounter.feature.recipes.domain.RecipesInteractorImpl
import ru.ari.caloriescounter.feature.recipes.domain.RecipesRepository
import ru.ari.caloriescounter.feature.stats.data.StatsRepositoryImpl
import ru.ari.caloriescounter.feature.stats.domain.StatsInteractor
import ru.ari.caloriescounter.feature.stats.domain.StatsInteractorImpl
import ru.ari.caloriescounter.feature.stats.domain.StatsRepository

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
    abstract fun bindRecipesInteractor(impl: RecipesInteractorImpl): RecipesInteractor
}

