package io.xorum.codeforceswatcher.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

internal actual fun CustomMainScope(): CoroutineScope = MainScope()
