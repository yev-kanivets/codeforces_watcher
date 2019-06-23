package com.bogdan.codeforceswatcher.room

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.migration.Migration

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE Contest (id INTEGER NOT NULL, " +
                "name TEXT NOT NULL, time INTEGER NOT NULL, phase TEXT NOT NULL, PRIMARY KEY(id))")
    }
}