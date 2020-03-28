//
//  Int+Extension.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 1/27/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import Foundation

extension Double {
    func secondsToDateString() -> String {
        let date = Date(timeIntervalSince1970: self)
        let dayTimePeriodFormatter = DateFormatter().apply {
            $0.dateFormat = "HH:mm MMM d, EEEE"
        }
        return dayTimePeriodFormatter.string(from: date)
    }
}
