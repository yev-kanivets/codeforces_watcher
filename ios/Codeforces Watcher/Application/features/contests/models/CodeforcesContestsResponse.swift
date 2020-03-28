//
//  CodeforcesContestsResponse.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 1/15/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import Foundation
import ObjectMapper

struct CodeforcesContestsResponse: Mappable {
    var contests: [CodeforcesContest] = []
    
    init?(map: Map) {}
    
    mutating func mapping(map: Map) {
        contests <- map["result"]
    }
}
