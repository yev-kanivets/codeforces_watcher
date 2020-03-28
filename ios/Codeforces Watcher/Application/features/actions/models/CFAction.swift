//
//  Action.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 12/31/19.
//  Copyright © 2019 xorum.io. All rights reserved.
//

import Foundation
import ObjectMapper

struct CFAction: Mappable {
    var timeSeconds: Int!
    var blogEntry: BlogEntry!
    var comment: Comment?
    
    init?(map: Map) {}
    
    mutating func mapping(map: Map) {
        timeSeconds <- map["timeSeconds"]
        blogEntry <- map["blogEntry"]
        comment <- map["comment"]
    }
}
