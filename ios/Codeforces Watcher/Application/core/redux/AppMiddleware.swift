//
// Created by Yevhenii Kanivets on 11/01/2020.
// Copyright (c) 2020 xorum.io. All rights reserved.
//

import Foundation
import ReSwift
import common

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
            default:
                break
            }
            
            next(action)
        }
    }
}
