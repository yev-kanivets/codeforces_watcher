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
        color = Pallete.black
    case "newbie":
        color = Pallete.grey
    case "pupil":
        color = Pallete.green
    case "specialist":
        color = Pallete.blueGreen
    case "expert":
        color = Pallete.blue
    case "candidate master":
        color = Pallete.purple
    case "master":
        color = Pallete.orange
    case "international master":
        color = Pallete.orange
    case "grandmaster":
        color = Pallete.red
    case "international grandmaster", "legendary grandmaster":
        color = Pallete.red
    default:
        color = Pallete.grey
    }
    
    let attributedText = NSMutableAttributedString.init(string: text)
    attributedText.addAttribute(NSAttributedString.Key.foregroundColor, value: color, range: NSRange(location: 0, length: text.count))
    
    if (rank == "legendary grandmaster") {
        attributedText.addAttribute(NSAttributedString.Key.foregroundColor, value: Pallete.black, range: NSRange(location: 0, length: 1))
    }
    
    return attributedText
}
