package redux

import com.squareup.sqldelight.db.SqlDriver
import io.xorum.codeforceswatcher.db.DatabaseController
import redux.middlewares.appMiddleware
import redux.middlewares.notificationMiddleware
import redux.middlewares.toastMiddleware
import redux.reducers.appReducer
import tw.geothings.rekotlin.Store

lateinit var sqlDriver: SqlDriver
val localizedStrings: MutableMap<String, String> = mutableMapOf()

val store by lazy {
    Store(
            reducer = ::appReducer,
            state = DatabaseController.fetchAppState(),
            middleware = listOf(
                    appMiddleware, notificationMiddleware, toastMiddleware
            )
    )
}
