//
//  ContestItem.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 1/15/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import Foundation
import UIKit
import common

struct ContestItem {
    var name: String!
    var startTime: String!
    var startDate: Date
    var endDate: Date
    var phase: String!
    var id: Int?
    var icon: UIImage!
    var link: String!
    var shareText: String!
    var site: Platform!
    
    init(_ contest: CodeforcesContest) {
        phase = contest.phase
        id = contest.id
        startTime = contest.startTimeSeconds.secondsToDateString()
        name = contest.name
        startDate = Date(timeIntervalSince1970: contest.startTimeSeconds)
        endDate = Date(timeIntervalSince1970: contest.startTimeSeconds + contest.durationSeconds)
        link = "\(codeforcesLink)/contestRegistration/\(id!)"
        shareText = """
        \(name!) - \(link!)
        Shared through Codeforces Watcher. Find it on App Store.
        """
        site = .codeforces
        icon = UIImage(named: site.rawValue)
    }
    
    init(_ contest: CommonContest) {
        name = contest.name
        startDate = contest.startTime.dateStringToDate()
        startTime = startDate.timeIntervalSince1970.secondsToDateString()
        endDate = contest.endTime.dateStringToDate()
        phase = contest.phase
        id = nil
        link = contest.link
        shareText = """
        \(name!) - \(link!)
        Shared through Codeforces Watcher. Find it on App Store.
        """
        site = contest.site
        icon = UIImage(named: site.rawValue)
    }
}
