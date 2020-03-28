//
//  FilterChangeAction.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 1/28/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import Foundation
import ReSwift

struct FilterChangeAction: Action {
    var platform: Platform
    var isOn: Bool
}
