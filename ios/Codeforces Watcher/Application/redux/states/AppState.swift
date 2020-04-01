//
// Created by Yevhenii Kanivets on 11/01/2020.
// Copyright (c) 2020 xorum.io. All rights reserved.
//

import Foundation
import ReSwift

struct AppState: StateType {
    var actions = ActionsState()
    var contests = ContestsState()
}

struct ActionsState: StateType {
    var actionItems: [ActionItem] = []
    var status: Status = Status.IDLE
    
    enum Status {
        case IDLE
        case PENDING
    }
}

struct ContestsState: StateType {
    var contestItems: [ContestItem] = []
    var status: Status = Status.IDLE
    var filters: [Platform: Bool] = [:]
    
    enum Status {
        case IDLE
        case PENDING
    }
    
    init() {
        filters = PersistenceController.getContestsFilters()
        
        if (filters.isEmpty) {
            for platform in Platform.allCases[..<(Platform.allCases.count - 2)] {
                filters[platform] = true
            }
        }
    }
}
