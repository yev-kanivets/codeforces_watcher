package io.xorum.codeforceswatcher.database

import io.xorum.codeforceswatcher.CWDatabase
import io.xorum.codeforceswatcher.redux.sqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

/**
 * https://github.com/cashapp/sqldelight/blob/d0fcd05115cd6d8cdd7ed91901b1a7676a16a637/sample/common/src/iosMain/kotlin/com/example/sqldelight/hockey/data/Db.kt
 */
fun initDatabase() {
    sqlDriver = NativeSqliteDriver(CWDatabase.Schema, "database.db")
}
