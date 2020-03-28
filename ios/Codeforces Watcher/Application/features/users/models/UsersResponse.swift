//
//  UsersResponse.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 1/6/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import Foundation
import ObjectMapper

struct UsersResponse: Mappable {
    var users: [User] = []
    
    init?(map: Map) {}
    
    mutating func mapping(map: Map) {
        users <- map["result"]
    }
}
