//
//  CodeforcesContest.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 1/15/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import Foundation
import ObjectMapper

struct CodeforcesContest: Mappable {
    var startTimeSeconds: Double!
    var durationSeconds: Double!
    var name: String!
    var phase: String!
    var id: Int!
    
    init?(map: Map) {}
    
    mutating func mapping(map: Map) {
        startTimeSeconds <- map["startTimeSeconds"]
        durationSeconds <- map["durationSeconds"]
        name <- map["name"]
        phase <- map["phase"]
        id <- map["id"]
    }
}

