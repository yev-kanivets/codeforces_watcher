//
//  User.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 1/6/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import Foundation
import UIKit

func colorTextByUserRank(text: String, rank: String?) -> NSMutableAttributedString {
    var color = UIColor()

    switch (rank) {
    case nil:
        color = Palette.black
    case "newbie":
        color = Palette.grey
    case "pupil":
        color = Palette.green
    case "specialist":
        color = Palette.blueGreen
    case "expert":
        color = Palette.blue
    case "candidate master":
        color = Palette.purple
    case "master":
        color = Palette.orange
    case "international master":
        color = Palette.orange
    case "grandmaster":
        color = Palette.red
    case "international grandmaster", "legendary grandmaster":
        color = Palette.red
    default:
        color = Palette.grey
    }

    let attributedText = NSMutableAttributedString.init(string: text)
    attributedText.addAttribute(NSAttributedString.Key.foregroundColor, value: color, range: NSRange(location: 0, length: text.count))

    if (rank == "legendary grandmaster") {
        attributedText.addAttribute(NSAttributedString.Key.foregroundColor, value: Palette.black, range: NSRange(location: 0, length: 1))
    }

    return attributedText
}
