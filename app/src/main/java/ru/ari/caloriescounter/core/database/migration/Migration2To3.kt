package ru.ari.caloriescounter.core.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val migration2To3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("DROP TABLE IF EXISTS calorie_entries")
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS calorie_entries (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                dayDate TEXT NOT NULL,
                mealType TEXT NOT NULL,
                productSource TEXT NOT NULL,
                productExternalId TEXT,
                productBarcode TEXT,
                productNameRu TEXT NOT NULL,
                productNameOriginal TEXT,
                caloriesPer100g REAL NOT NULL,
                proteinPer100g REAL NOT NULL,
                fatPer100g REAL NOT NULL,
                carbsPer100g REAL NOT NULL,
                portionGrams REAL NOT NULL
            )
            """.trimIndent(),
        )
    }
}
