//
// Created by Yevhenii Kanivets on 11/01/2020.
// Copyright (c) 2020 xorum.io. All rights reserved.
//

import Foundation
import UIKit

extension UITableViewCell {

    class var nibName: String {
        return String(describing: self)
    }

    class var reuseIdentifier: String {
        return String(describing: self)
    }

    class func register(to tableView: UITableView) {
        tableView.register(UINib.init(nibName: self.nibName, bundle: nil), forCellReuseIdentifier: self.reuseIdentifier)
    }
}
