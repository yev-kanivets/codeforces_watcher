//
//  MessageAction.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 1/22/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import Foundation
import ReSwift

protocol MessageAction: Action {
    var message: String { get set }
}
