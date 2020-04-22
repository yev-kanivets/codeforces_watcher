//
//  UsersPickerViewAdapter.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 4/16/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import Foundation
import UIKit

class UsersPickerViewAdapter: NSObject, UIPickerViewDelegate, UIPickerViewDataSource {
    
    var options: [String] = []
    var optionSelected: ((Int) -> ())?
    
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return options.count
    }
    
    func pickerView(_ pickerView: UIPickerView, viewForRow row: Int, forComponent component: Int, reusing view: UIView?) -> UIView {
        return UILabel().apply {
            $0.text = options[row]
            $0.textColor = Palette.colorPrimary
            $0.textAlignment = .center
            $0.font = Font.textPageTitle
        }
    }
    
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        optionSelected?(row)
    }
}
