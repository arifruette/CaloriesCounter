package ru.ari.caloriescounter.core.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val migration1To2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS weight_profile (
                id INTEGER NOT NULL,
                currentWeightKg REAL NOT NULL,
                targetWeightKg REAL NOT NULL,
                PRIMARY KEY(id)
            )
            """.trimIndent(),
        )
    }
}
