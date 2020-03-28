//
//  ContestsRequests.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 1/16/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import Foundation
import Alamofire
import ReSwift
import ObjectMapper

struct ContestsRequests {
    struct FetchCodeforcesContests: Request {
        func execute() {
            let request = Alamofire.request("\(codeforcesApiLink)contest.list",  parameters: ["lang": "en"])
            request.validate().responseString { response in
                switch response.result {
                case .success:
                    self.handleSuccess(response)
                case .failure:
                    self.handleFailure()
                }
            }
        }
        
        func handleSuccess(_ response: DataResponse <String>) {
            if let json = response.result.value, let contestsResponse = CodeforcesContestsResponse(JSONString: json)?.contests {
                let contestItems = mapContests(contests: contestsResponse)
                
                store.dispatch(Success(contestItems: contestItems))
            } else {
                self.handleFailure()
            }
        }
        
        func mapContests(contests: [CodeforcesContest]) -> [ContestItem] {
            var contestItems: [ContestItem] = []
            
            for contest in contests {
                contestItems.append(ContestItem(contest))
            }
            
            return contestItems.reversed()
        }
        
        func handleFailure() {
            store.dispatch(Failure(message: "No connection".localized))
        }
        
        struct Success: Action {
            var contestItems: [ContestItem]
        }
        
        struct Failure: MessageAction {
            var message: String
        }
    }
    
    struct FetchAllContests: Request {
        func execute() {
            let request = Alamofire.request("\(kontestsApiLink)all")
            request.validate().responseString { response in
                
                switch response.result {
                case .success:
                    self.handleSuccess(response)
                case .failure:
                    self.handleFailure()
                }
            }
        }
        
        func handleSuccess(_ response: DataResponse <String>) {
            if let json = response.result.value, let contestsResponse = Mapper<CommonContest> ().mapArray(JSONString: json) {
                    print("Success fetching all contests")
                    let contestItems = mapContests(contests: contestsResponse)
                    
                    store.dispatch(Success(contestItems: contestItems))
            } else {
                self.handleFailure()
            }
        }
        
        func mapContests(contests: [CommonContest]) -> [ContestItem] {
            var contestItems: [ContestItem] = []
            
            for contest in contests {
                if (contest.site != .codeforces && contest.site != .codeforcesGym && contest.site != .A2OJ) {
                    contestItems.append(ContestItem(contest))
                }
            }
            
            return contestItems
        }
        
        func handleFailure() {
            store.dispatch(Failure(message: "No connection".localized))
        }
        
        struct Success: Action {
            var contestItems: [ContestItem]
        }
        
        struct Failure: MessageAction {
            var message: String
        }
    }
}
