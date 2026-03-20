package ru.ari.caloriescounter.core.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val migration4To5 = object : Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS manual_products (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                nameRu TEXT NOT NULL,
                caloriesPer100g REAL NOT NULL,
                proteinPer100g REAL NOT NULL,
                fatPer100g REAL NOT NULL,
                carbsPer100g REAL NOT NULL,
                createdAtEpochMillis INTEGER NOT NULL
            )
            """.trimIndent(),
        )
        db.execSQL(
            """
            CREATE INDEX IF NOT EXISTS index_manual_products_createdAtEpochMillis
            ON manual_products(createdAtEpochMillis)
            """.trimIndent(),
        )
    }
}
