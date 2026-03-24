package ru.ari.caloriescounter.core.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import ru.ari.caloriescounter.core.database.AppDatabase
import ru.ari.caloriescounter.core.database.dao.CalorieEntryDao
import ru.ari.caloriescounter.core.database.dao.DayMealSlotDao
import ru.ari.caloriescounter.core.database.dao.ManualProductDao
import ru.ari.caloriescounter.core.database.dao.NutritionGoalsDao
import ru.ari.caloriescounter.core.database.dao.WeightProfileDao
import ru.ari.caloriescounter.core.database.migration.migration1To2
import ru.ari.caloriescounter.core.database.migration.migration2To3
import ru.ari.caloriescounter.core.database.migration.migration3To4
import ru.ari.caloriescounter.core.database.migration.migration4To5
import ru.ari.caloriescounter.core.database.migration.migration5To6

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "calories-counter.db")
            .addMigrations(migration1To2)
            .addMigrations(migration2To3)
            .addMigrations(migration3To4)
            .addMigrations(migration4To5)
            .addMigrations(migration5To6)
            .build()

    @Provides
    fun provideCalorieEntryDao(database: AppDatabase): CalorieEntryDao = database.calorieEntryDao()

    @Provides
    fun provideWeightProfileDao(database: AppDatabase): WeightProfileDao = database.weightProfileDao()

    @Provides
    fun provideNutritionGoalsDao(database: AppDatabase): NutritionGoalsDao = database.nutritionGoalsDao()

    @Provides
    fun provideManualProductDao(database: AppDatabase): ManualProductDao = database.manualProductDao()

    @Provides
    fun provideDayMealSlotDao(database: AppDatabase): DayMealSlotDao = database.dayMealSlotDao()
}
