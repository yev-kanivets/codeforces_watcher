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
    var actions: [CFAction] = []
    
    var onActionClick: ((String, String) -> ())?

    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }

    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if (actions.isEmpty) {
            return 1
        }
        
        return actions.count
    }

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if (actions.isEmpty) {
            return tableView.dequeueReusableCell(cellType: NoActionsTableViewCell.self)
        }
        
        let action = actions[indexPath.row]
        let isComment = action.comment != nil
        
        if (isComment) {
            return tableView.dequeueReusableCell(cellType: CommentTableViewCell.self).apply {
                $0.bind(action)
            }
        } else {
            return tableView.dequeueReusableCell(cellType: BlogEntryTableViewCell.self).apply {
                $0.bind(action)
            }
        }
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if (actions.isEmpty) {
            return
        }
        
        /*let action = actions[indexPath.row]
        let shareText = buildShareText(action.blogEntry.title, action.link)
        
        onActionClick?(action.link, action.shareText)
         
        uncomment when Bohdan will add link
        */
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if (actions.isEmpty) {
            return tableView.frame.height
        } else {
            return UITableView.automaticDimension
        }
    }
    
    func tableView(_ tableView: UITableView, estimatedHeightForRowAt indexPath: IndexPath) -> CGFloat {
        return 122
    }
}
