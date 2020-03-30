//
//  PersistenceController.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 1/30/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import Foundation

struct PersistenceController {
    static func saveContestsFilters(filters: [Platform: Bool]) {
        var mappedFilters: [String: Bool] = [:]
        
        for param in filters {
            mappedFilters[param.key.rawValue] = param.value
        }
        
        UserDefaults.standard.setValue(mappedFilters, forKey: "filters")
    }
    
    static func getContestsFilters() -> [Platform: Bool] {
        if let savedFilters = (UserDefaults.standard.value(forKey: "filters")) as? [String: Bool] {
            var mappedFilters: [Platform: Bool] = [:]
            
            for param in savedFilters {
                mappedFilters[Platform(rawValue: param.key)!] = param.value
            }
            
            return mappedFilters
        } else {
            return [:]
        }
    }
    
    static func clearAllKeys() {
        let domain = Bundle.main.bundleIdentifier!
        UserDefaults.standard.removePersistentDomain(forName: domain)
        UserDefaults.standard.synchronize()
    }
}
