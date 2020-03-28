//
//  ProblemsResponse.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 1/17/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import Foundation
import ObjectMapper

struct ProblemsResponse: Mappable {
    var problems: [Problem] = []
    
    init?(map: Map) {}
    
    mutating func mapping(map: Map) {
        problems <- map["result.problems"]
    }
}
