package io.xorum.codeforceswatcher.redux

import tw.geothings.rekotlin.Action

interface ToastAction : Action {

    val message: Message
}

sealed class Message {

    object NoConnection : Message()

    object UserAlreadyAdded : Message()

    object  FailedToFetchUser : Message()

    object None : Message()

    data class Custom(val message: String) : Message()
}
