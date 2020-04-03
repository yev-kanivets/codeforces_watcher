//
//  FetchActionsRequest.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 1/2/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import Foundation
import Alamofire
import ReSwift
/*
struct ActionsRequests {
    struct FetchActions: Request {
        func execute() {
            let request = Alamofire.request("\(codeforcesApiLink)recentActions?maxCount=100")
            
            request.validate().responseString { response in
                
                switch response.result {
                case .success:
                    self.handleSuccess(response)
                case .failure:
                    self.handleFailure()
                }
            }
        }
        
        func mapActions(users: [User], actions: [CFAction]) -> [ActionItem] {
            var actionItems: [ActionItem] = []
            
            for (index, action) in actions.enumerated() {
                let isComment: Bool = (action.comment == nil ? false : true)
                let userRank = users[index].rank
                let avatar = users[index].avatar!
                
                if (isComment) {
                    actionItems.append(
                        ActionItem.comment(ActionItem.CommentItem(action: action, rank: userRank, avatar: avatar)))
                } else {
                    if (action.timeSeconds != action.blogEntry.creationTimeSeconds &&
                        action.timeSeconds != action.blogEntry.modificationTimeSeconds) {
                        
                        continue;
                    }
                    
                    actionItems.append(
                        ActionItem.blogEntry(ActionItem.BlogEntryItem(action: action, rank: userRank, avatar: avatar)))
                 }
            }
            
            return actionItems
        }
        
        func handleSuccess(_ response: DataResponse <String>) {
            if let json = response.result.value, let actionsResponse = ActionsResponse(JSONString: json)?.actions {
                print("Success fetching actions")

                let handlesString = FetchUsersRequest.buildHandlesString(actions: actionsResponse)
                    
                FetchUsersRequest().execute(handles: handlesString) { usersResponse in
                    let actionItems: [ActionItem] = self.mapActions(users: usersResponse, actions: actionsResponse)
                    
                    store.dispatch(Success(actionItems: actionItems))
                }
            } else {
                self.handleFailure()
            }
        }
        
        func handleFailure() {
            store.dispatch(Failure(message: "No connection".localized))
        }
        
        struct Success: Action {
            var actionItems: [ActionItem]
        }
        
        struct Failure: MessageAction {
            var message: String
        }
    }
}
*/
