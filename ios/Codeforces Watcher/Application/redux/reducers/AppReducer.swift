//
// Created by Yevhenii Kanivets on 11/01/2020.
// Copyright (c) 2020 xorum.io. All rights reserved.
//

import Foundation
import ReSwift
/*
func appReducer(action: Action, state: AppState?) -> AppState {
    guard let state = state else { fatalError() }

    return AppState(
        contests: contestsReducer(action, state)
    )
}

func contestsReducer(_ action: Action, _ state: AppState) -> ContestsState {
    var newState = state.contests
    switch (action) {
    case _ as ContestsRequests.FetchAllContests,
         _ as ContestsRequests.FetchCodeforcesContests:
        newState.status = ContestsState.Status.PENDING
        
    case let action as ContestsRequests.FetchCodeforcesContests.Success:
        newState.contestItems = action.contestItems
        
    case _ as ContestsRequests.FetchCodeforcesContests.Failure:
        newState.status = ContestsState.Status.IDLE
    
    case let action as ContestsRequests.FetchAllContests.Success:
        newState.contestItems += action.contestItems
        newState.status = ContestsState.Status.IDLE
        
    case _ as ContestsRequests.FetchAllContests.Failure:
        newState.status = ContestsState.Status.IDLE
    case let action as FilterChangeAction:
        newState.filters[action.platform] = action.isOn
        PersistenceController.saveContestsFilters(filters: newState.filters)
    default:
        break
    }
    return newState
}
*/
