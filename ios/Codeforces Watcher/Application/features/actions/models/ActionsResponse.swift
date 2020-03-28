//
//  ActionsResponse.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 12/31/19.
//  Copyright © 2019 xorum.io. All rights reserved.
//

import Foundation
import ObjectMapper

struct ActionsResponse: Mappable {
    var actions: [CFAction] = []
    
    init?(map: Map) {}
    
    mutating func mapping(map: Map) {
        actions <- map["result"]
    }
}
