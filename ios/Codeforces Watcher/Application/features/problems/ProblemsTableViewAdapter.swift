//
//  ProblemsTableViewAdapter.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 1/17/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import Foundation
import UIKit
import common

class ProblemsTableViewAdapter: NSObject, UITableViewDelegate, UITableViewDataSource {
    
    var problems: [Problem] = []
    var visibleProblems: [Problem] = []

    var onProblemClick: ((String, String) -> ())?

    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    var isFavourite = false

    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if (visibleProblems.isEmpty) {
            return 1
        }
        
        return visibleProblems.count
    }

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if (visibleProblems.isEmpty) {
            return tableView.dequeueReusableCell(cellType: NoProblemsTableViewCell.self).apply {
                $0.bind(isFavourite ? "no_favourite_problems_explanation" : "Problems are on the way to your device...")
            }
        }
        
        return tableView.dequeueReusableCell(cellType: ProblemTableViewCell.self).apply {
            $0.bind(visibleProblems[indexPath.row])
        }
    }

    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if (visibleProblems.isEmpty) {
            return
        }

        let problem = visibleProblems[indexPath.row]
        let shareText = buildShareText(problem.name, problem.link)
        onProblemClick?(problem.link, shareText)
    }

    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if (visibleProblems.isEmpty) {
            return tableView.frame.height - 2 * tableView.tableHeaderView!.frame.height
        } else {
            return 63
        }
    }
}
