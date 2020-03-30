//
//  ProblemsRequests.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 1/20/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import Foundation
import Alamofire
import ReSwift

struct ProblemsRequests {
    struct FetchProblems: Request {
        func execute() {
            let request = Alamofire.request("\(codeforcesApiLink)problemset.problems", parameters: ["lang": "en"])
            
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
            if let json = response.result.value, let problemsResponse = ProblemsResponse(JSONString: json)?.problems {
                print("Success fetching english problems")
                
                let englishProblemItems = mapProblems(problems: problemsResponse)
                
                ProblemsRequests.FetchRussianProblems().execute() { russianProblemItems in
                    let problemItems: [ProblemItem] = self.mergeProblemItems( englishProblemItems, russianProblemItems)
                    
                    store.dispatch(Success(problemItems: problemItems))
                }
            } else {
                self.handleFailure()
            }
        }
        
        func mapProblems(problems: [Problem]) -> [ProblemItem] {
            var contestNameByIdDict: [Int: String] = [:]
        
            for contest in store.state.contests.contestItems {
                if let id = contest.id {
                    contestNameByIdDict[id] = contest.name
                }
            }
            
            var problemItems: [ProblemItem] = []
            
            for problem in problems {
                problemItems.append(ProblemItem(problem: problem, roundName: contestNameByIdDict[problem.contestId]))
            }
            
            return problemItems
        }
        
        func mergeProblemItems(_ englishProblemItems: [ProblemItem], _ russianProblemItems: [ProblemItem]) -> [ProblemItem] {
            var problemItems: [ProblemItem] = []
            
            for index in 0..<englishProblemItems.count {
                problemItems.append(ProblemItem(englishProblemItem: englishProblemItems[index], russianTitle: russianProblemItems[index].title))
            }
            
            return problemItems
        }
        
        func handleFailure() {
            store.dispatch(Failure(message: "No connection".localized))
        }
        
        struct Success: Action {
            var problemItems: [ProblemItem]
        }
        
        struct Failure: MessageAction {
            var message: String
        }
    }
    
    struct FetchRussianProblems {
        func execute(completion: @escaping ([ProblemItem]) -> ()) {
            let request = Alamofire.request("\(codeforcesApiLink)problemset.problems", parameters: ["lang": "ru"])
            
            request.validate().responseString { response in
                
                switch response.result {
                case .success:
                    self.handleSuccess(response, completion)
                case .failure:
                    self.handleFailure()
                }
            }
        }
        
        func handleSuccess(_ response: DataResponse <String>, _ completion: @escaping ([ProblemItem]) -> ()) {
            if let json = response.result.value, let problemsResponse = ProblemsResponse(JSONString: json)?.problems {
                print("Success fetching russian problems")
                
                let problemItems = mapProblems(problems: problemsResponse)
                
                DispatchQueue.main.async { completion(problemItems) }
            } else {
                self.handleFailure()
            }
        }
        
        func mapProblems(problems: [Problem]) -> [ProblemItem] {
            var contestNameByIdDict: [Int: String] = [:]
        
            for contest in store.state.contests.contestItems {
                if let id = contest.id {
                    contestNameByIdDict[id] = contest.name
                }
            }
            
            var problemItems: [ProblemItem] = []
            
            for problem in problems {
                problemItems.append(ProblemItem(problem: problem, roundName: contestNameByIdDict[problem.contestId]))
            }
            
            return problemItems
        }
        
        func handleFailure() {
            store.dispatch(Failure(message: "No connection".localized))
        }
        
        struct Success: Action {
            var problemItems: [ProblemItem]
        }
        
        struct Failure: MessageAction {
            var message: String
        }
    }
}
