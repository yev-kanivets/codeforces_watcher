//
//  ProblemsTableViewAdapter.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 1/17/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import Foundation
import UIKit

class ProblemsTableViewAdapter: NSObject, UITableViewDelegate, UITableViewDataSource {
    var problemItems: [ProblemItem] = []
    
    var onProblemClick: ((String, String) -> ())?
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }

    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if (problemItems.isEmpty) {
            return 1
        }
        
        return problemItems.count
    }

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if (problemItems.isEmpty) {
            return tableView.dequeueReusableCell(cellType: NoProblemsTableViewCell.self)
        }
        
        return tableView.dequeueReusableCell(cellType: ProblemTableViewCell.self).apply {
            $0.bind(problemItems[indexPath.row])
        }
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if (problemItems.isEmpty) {
            return
        }
        
        let problem = problemItems[indexPath.row]
        onProblemClick?(problem.link, problem.shareText)
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if (problemItems.isEmpty) {
            return tableView.frame.height - 2 * tableView.tableHeaderView!.frame.height
        } else {
            return 63
        }
    }
}
