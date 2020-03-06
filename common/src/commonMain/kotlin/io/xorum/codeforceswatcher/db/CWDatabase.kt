package io.xorum.codeforceswatcher.db

import io.xorum.codeforceswatcher.CWDatabase
import redux.sqlDriver

val database by lazy { CWDatabase(sqlDriver) }
