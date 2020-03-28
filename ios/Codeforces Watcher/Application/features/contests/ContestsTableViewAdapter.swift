//
//  ContestsTableViewAdapter.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 1/15/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import Foundation
import UIKit

class ContestsTableViewAdapter: NSObject, UITableViewDelegate, UITableViewDataSource {
    var contestItems: [ContestItem] = []
    
    var onCalendarTap: ((ContestItem) -> ())?
    var onContestClick: ((ContestItem) -> ())!
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }

    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if (contestItems.isEmpty) {
            return 1
        }
        
        return contestItems.count
    }

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if (contestItems.isEmpty) {
            return tableView.dequeueReusableCell(cellType: NoContestsTableViewCell.self)
        }
        
        return tableView.dequeueReusableCell(cellType: ContestTableViewCell.self).apply {
            $0.bind(contestItems[indexPath.row]) {
                self.onCalendarTap?(self.contestItems[indexPath.row])
            }
        }
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        return onContestClick(contestItems[indexPath.row])
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if (contestItems.isEmpty) {
            return tableView.frame.height - 2 * tableView.tableHeaderView!.frame.height
        } else {
            return 63
        }
    }
}
