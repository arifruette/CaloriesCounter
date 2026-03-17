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
import ru.ari.caloriescounter.core.database.migration.migration1To2
import ru.ari.caloriescounter.core.database.migration.migration2To3
import ru.ari.caloriescounter.core.database.dao.CalorieEntryDao
import ru.ari.caloriescounter.core.database.dao.WeightProfileDao

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "calories-counter.db")
            .addMigrations(migration1To2)
            .addMigrations(migration2To3)
            .build()

    @Provides
    fun provideCalorieEntryDao(database: AppDatabase): CalorieEntryDao = database.calorieEntryDao()

    @Provides
    fun provideWeightProfileDao(database: AppDatabase): WeightProfileDao = database.weightProfileDao()
}
