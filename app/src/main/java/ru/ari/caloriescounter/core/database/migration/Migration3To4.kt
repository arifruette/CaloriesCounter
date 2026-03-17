package ru.ari.caloriescounter.core.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val migration3To4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS nutrition_goals (
                id INTEGER NOT NULL PRIMARY KEY,
                calories INTEGER NOT NULL,
                protein REAL NOT NULL,
                fat REAL NOT NULL,
                carbs REAL NOT NULL
            )
            """.trimIndent(),
        )
    }
}
