package io.xorum.codeforceswatcher.db

import io.xorum.codeforceswatcher.CWDatabase
import io.xorum.codeforceswatcher.redux.sqlDriver

val database = CWDatabase(sqlDriver)
