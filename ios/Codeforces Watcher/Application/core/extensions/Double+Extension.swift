//
//  Int+Extension.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 1/27/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import Foundation

extension Double {

    func secondsToContestDateString() -> String {
        let date = Date(timeIntervalSince1970: self)
        let dayTimePeriodFormatter = DateFormatter().apply {
            $0.dateFormat = "contest_date_format".localized
        }
        return dayTimePeriodFormatter.string(from: date)
    }
    
    func secondsToUserUpdateDateString() -> String {
        let date = Date(timeIntervalSince1970: self)
        let dayTimePeriodFormatter = DateFormatter().apply {
            $0.dateFormat = "user_date_format".localized
        }
        return dayTimePeriodFormatter.string(from: date)
    }
}
