package com.bogdan.codeforceswatcher.room

import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.migration.Migration

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE Contest (id INTEGER NOT NULL, " +
                "name TEXT NOT NULL, time INTEGER NOT NULL, phase TEXT NOT NULL, PRIMARY KEY(id))")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE Contest ADD COLUMN duration INTEGER DEFAULT 0 NOT NULL")
    }
}