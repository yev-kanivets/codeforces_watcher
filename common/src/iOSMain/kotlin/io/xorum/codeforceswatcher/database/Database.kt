package io.xorum.codeforceswatcher.database

import io.xorum.codeforceswatcher.CWDatabase
import io.xorum.codeforceswatcher.redux.sqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

fun initDatabase() {
    sqlDriver = NativeSqliteDriver(CWDatabase.Schema, "database")
}
