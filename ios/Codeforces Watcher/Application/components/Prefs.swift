//
//  Prefs.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 4/3/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import Foundation
import common

class Prefs: Settings {
    func readProblemsIsFavourite() -> Bool {
        return false
    }
    
    func readSpinnerSortPosition() -> Int32 {
        return 0
    }
    
    func writeProblemsIsFavourite(isFavourite: Bool) {
        
    }
    
    func writeSpinnerSortPosition(spinnerSortPosition: Int32) {
        
    }
}
