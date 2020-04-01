//
// Created by Yevhenii Kanivets on 11/01/2020.
// Copyright (c) 2020 xorum.io. All rights reserved.
//

import Foundation
import ReSwift
import common

let notificationStatusWindow = NotificationStatusWindow(frame: CGRect.zero)

let appMiddleware: Middleware<AppState> = { dispatch, getState in
    return { next in
        return { action in
            switch action {
            case let request as Request:
                request.execute()
            
            case _ as ContestsRequests.FetchCodeforcesContests.Failure:
                store.dispatch(ContestsRequests.FetchAllContests())
            // Need to get contest name by id in Problems tab
            case _ as ContestsRequests.FetchCodeforcesContests.Success:
                store.dispatch(ContestsRequests.FetchAllContests())
                newStore.dispatch(action: ProblemsRequests.FetchProblems(isInitializedByUser: false))
                
            case let messageAction as MessageAction:
                notificationStatusWindow.fireNotification(message: messageAction.message)
            default:
                break
            }
            
            next(action)
        }
    }
}
