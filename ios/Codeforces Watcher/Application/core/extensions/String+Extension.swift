//
// Created by Yevhenii Kanivets on 11/01/2020.
// Copyright (c) 2020 xorum.io. All rights reserved.
//

import Foundation

extension String {
    
    mutating func deleteHtmlTags() {
        self = self.replacingOccurrences(of: "<[^>]+>", with: "", options: .regularExpression, range: nil)
    }

    func beautify() -> String {
        var newString = self
        newString.deleteHtmlTags()
        newString = newString.removingHTMLEntities
        newString = newString.replacingOccurrences(of: "$$$", with: "")

        return newString
    }

    var localized: String {
        let string = NSLocalizedString(self, comment: "")

        if
            string == self, //the translation was not found
            let baseLanguagePath = Bundle.main.path(forResource: "Base", ofType: "lproj"),
            let baseLangBundle = Bundle(path: baseLanguagePath) {

            return NSLocalizedString(self, bundle: baseLangBundle, comment: "")
        } else {
            return string
        }
    }
    
    func dateStringToDate() -> Date {
        let formatter = DateFormatter().apply {
            $0.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
        }
        return formatter.date(from: self)!
    }
}
