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
    
    func writeContestsFilters(filters: Set<String>) {
        UserDefaults.standard.setValue(Array(filters), forKey: "contestsFilters")
    }

    func readProblemsIsFavourite() -> Bool {
        if let savedIsFavourite = (UserDefaults.standard.value(forKey: "isFavouriteProblems")) as? Bool {
            return savedIsFavourite
        } else {
            return false
        }
    }
    
    func writeProblemsIsFavourite(isFavourite: Bool) {
<<<<<<< HEAD:ios/Codeforces Watcher/Application/utils/Prefs.swift
        UserDefaults.standard.setValue(isFavourite, forKey: "isFavouriteProblems")
    }

    func readSpinnerSortPosition() -> Int32 {
        return 0
=======
        
>>>>>>> #116. Implement pinned post.:ios/Codeforces Watcher/Application/utils/Prefs.swift
    }

    func writeSpinnerSortPosition(spinnerSortPosition: Int32) {

    }

    func readPinnedPostLink() -> String {
        if let pinnedPostLink = (UserDefaults.standard.value(forKey: "pinnedPostLink")) as? String {
            return pinnedPostLink
        } else {
            return ""
        }
    }

    func writePinnedPostLink(pinnedPostLink: String) {
<<<<<<< HEAD:ios/Codeforces Watcher/Application/utils/Prefs.swift
        
    }
    
    func resetAllDefaults() {
        let domain = Bundle.main.bundleIdentifier!
        UserDefaults.standard.removePersistentDomain(forName: domain)
        UserDefaults.standard.synchronize()
=======
        UserDefaults.standard.setValue(pinnedPostLink, forKey: "pinnedPostLink")
>>>>>>> #116. Implement pinned post.:ios/Codeforces Watcher/Application/utils/Prefs.swift
    }
}

