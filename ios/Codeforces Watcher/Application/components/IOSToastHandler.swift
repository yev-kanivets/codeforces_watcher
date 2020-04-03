//
//  IOSToastHandler.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 4/2/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import common
import Toaster

class IOSToastHandler: ToastHandler {
    func handle(message: Message) {
        var text: String?
        
        switch (message) {
        case _ as Message.NoConnection:
            text = "No connection".localized
        /*case _ as Message.UserAlreadyAdded:
            getString(R.string.user_already_added)
        case _ as Message.FailedToFetchUser:
            getString(R.string.failed_to_fetch_users)*/
        case _ as Message.None:
            text = nil
        case let message as Message.Custom:
            text = message.message
        default:
            break
        }
        
        if let text = text {
            Toast(text: text).show()
        }
    }
}
