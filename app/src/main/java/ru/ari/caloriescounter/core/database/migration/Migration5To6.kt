package ru.ari.caloriescounter.core.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val migration5To6 = object : Migration(5, 6) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("UPDATE calorie_entries SET mealType = 'breakfast' WHERE mealType = 'BREAKFAST'")
        db.execSQL("UPDATE calorie_entries SET mealType = 'lunch' WHERE mealType = 'LUNCH'")
        db.execSQL("UPDATE calorie_entries SET mealType = 'dinner' WHERE mealType = 'DINNER'")
        db.execSQL("UPDATE calorie_entries SET mealType = 'snack' WHERE mealType = 'SNACK'")
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS day_meal_slots (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                dayDate TEXT NOT NULL,
                mealKey TEXT NOT NULL,
                title TEXT NOT NULL,
                sortOrder INTEGER NOT NULL,
                isBase INTEGER NOT NULL
            )
            """.trimIndent(),
        )
        db.execSQL(
            """
            CREATE UNIQUE INDEX IF NOT EXISTS index_day_meal_slots_dayDate_mealKey
            ON day_meal_slots(dayDate, mealKey)
            """.trimIndent(),
        )
        db.execSQL(
            """
            CREATE INDEX IF NOT EXISTS index_day_meal_slots_dayDate_sortOrder
            ON day_meal_slots(dayDate, sortOrder)
            """.trimIndent(),
        )
    }
}
