//
//  CommonContest.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 1/27/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import Foundation
import ObjectMapper

struct CommonContest: Mappable {
    var name: String!
    var phase: String!
    var startTime: String!
    var endTime: String!
    var durationSeconds: Double!
    var site: Platform!
    var link: String!
    
    init?(map: Map) {}
    
    mutating func mapping(map: Map) {
        name <- map["name"]
        phase <- map["status"]
        startTime <- map["start_time"]
        durationSeconds <- map["duration"]
        site <- map["site"]
        endTime <- map["end_time"]
        link <- map["url"]
    }
}
