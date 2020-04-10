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
    
    func readContestsFilters() -> Set<String> {
        if let savedFilters = (UserDefaults.standard.value(forKey: "contestsFilters")) as? Array<String> {
            return Set(savedFilters)
        } else {
            return Platform.Companion().defaultFilterValueToSave
        }
    }

    func readProblemsIsFavourite() -> Bool {
        return false
    }

    func readSpinnerSortPosition() -> Int32 {
        return 0
    }

    func writeContestsFilters(filters: Set<String>) {
        UserDefaults.standard.setValue(Array(filters), forKey: "contestsFilters")
    }

    func writeProblemsIsFavourite(isFavourite: Bool) {

    }

    func writeSpinnerSortPosition(spinnerSortPosition: Int32) {

    }

    func resetAllDefaults() {
        let domain = Bundle.main.bundleIdentifier!
        UserDefaults.standard.removePersistentDomain(forName: domain)
        UserDefaults.standard.synchronize()
    }

    func readPinnedPostLink() -> String {
        return ""
    }

    func writePinnedPostLink(pinnedPostLink: String) {

    }
}

