//
//  Problem.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 1/17/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import Foundation
import ObjectMapper

struct Problem: Mappable {
    var name: String!
    var contestId: Int!
    var index: String!
    
    init?(map: Map) {}
    
    mutating func mapping(map: Map) {
        name <- map["name"]
        contestId <- map["contestId"]
        index <- map["index"]
    }
}
