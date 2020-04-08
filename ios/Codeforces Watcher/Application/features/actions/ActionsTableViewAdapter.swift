//
//  TableViewController.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 1/2/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import UIKit
import common

class ActionsTableViewAdapter: NSObject, UITableViewDelegate, UITableViewDataSource {
    var actionItems: [ActionItem] = []
    
    var onActionClick: ((String, String) -> ())?

    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }

    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if (actionItems.isEmpty) {
            return 1
        }
        
        return actionItems.count
    }

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if (actionItems.isEmpty) {
            return tableView.dequeueReusableCell(cellType: NoActionsTableViewCell.self)
        }
        
        switch (actionItems[indexPath.row]) {
        case .comment(let actionItem):
            return tableView.dequeueReusableCell(cellType: CommentTableViewCell.self).apply {
                $0.bind(actionItem: actionItem)
            }
        case .blogEntry(let actionItem):
            return tableView.dequeueReusableCell(cellType: BlogEntryTableViewCell.self).apply {
                $0.bind(actionItem: actionItem)
            }
        }
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if (actionItems.isEmpty) {
            return
        }
        
        switch (actionItems[indexPath.row]) {
        case .comment(let actionItem):
            onActionClick?(actionItem.link, actionItem.shareText)
        case .blogEntry(let actionItem):
            onActionClick?(actionItem.link, actionItem.shareText)
        }
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if (actionItems.isEmpty) {
            return tableView.frame.height
        } else {
            return UITableView.automaticDimension
        }
    }
    
    func tableView(_ tableView: UITableView, estimatedHeightForRowAt indexPath: IndexPath) -> CGFloat {
        return 122
    }
}

class Prefs : Settings {

    func readContestsFilters() -> Set<String> {
        fatalError()
    }

    func readProblemsIsFavourite() -> Bool {
        fatalError()
    }

    func readSpinnerSortPosition() -> Int32 {
        fatalError()
    }

    func writeContestsFilters(filters: Set<String>) {
        fatalError()
    }

    func writeProblemsIsFavourite(isFavourite: Bool) {
        fatalError()
    }

    func writeSpinnerSortPosition(spinnerSortPosition: Int32) {
        fatalError()
    }


}

