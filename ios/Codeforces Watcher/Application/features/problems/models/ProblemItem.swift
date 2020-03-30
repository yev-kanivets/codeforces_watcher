//
//  ProblemItem.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 1/17/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import Foundation

struct ProblemItem {
    var shareText: String
    var title: String!
    var round: String!
    var link: String
    var englishTitle: String!
    var russianTitle: String!
    
    init(problem: Problem, roundName: String?) {
        title = "\(problem.contestId!)\(problem.index!): \(problem.name!)"
        round = roundName
        link = "\(codeforcesLink)contest/\(problem.contestId!)/problem/\(problem.index!)"
        shareText = """
        \(title!) - \(link)
        
        Shared through Codeforces Watcher. Find it on App Store.
        """
    }
    
    init(englishProblemItem: ProblemItem, russianTitle: String) {
        self = englishProblemItem
        englishTitle = englishProblemItem.title
        self.russianTitle = russianTitle
    }
}
