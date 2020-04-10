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
    var favouriteProblems: [Problem] = []

    var onProblemClick: ((String, String) -> ())?

    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    var isFavourite: Bool {
        get {
            return store.state.problems.isFavourite
        }
    }

    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if (isFavourite && favouriteProblems.isEmpty || !isFavourite && problems.isEmpty) {
            return 1
        }
        
        return isFavourite ? favouriteProblems.count : problems.count
    }

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if (isFavourite) {
            if (favouriteProblems.isEmpty) {
                return tableView.dequeueReusableCell(cellType: NoProblemsTableViewCell.self).apply {
                    $0.bind("no_favourite_problems_explanation")
                }
            }
            return tableView.dequeueReusableCell(cellType: ProblemTableViewCell.self).apply {
                $0.bind(favouriteProblems[indexPath.row])
            }
        } else {
            if (problems.isEmpty) {
                return tableView.dequeueReusableCell(cellType: NoProblemsTableViewCell.self).apply {
                    $0.bind("Problems are on the way to your device...")
                }
            }

            return tableView.dequeueReusableCell(cellType: ProblemTableViewCell.self).apply {
                $0.bind(problems[indexPath.row])
            }
        }
    }

    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if (isFavourite && favouriteProblems.isEmpty || !isFavourite && problems.isEmpty) {
            return
        }

        let problem = isFavourite ? favouriteProblems[indexPath.row] : problems[indexPath.row]
        let shareText = buildShareText(problem.name, problem.link)
        onProblemClick?(problem.link, shareText)
    }

    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if (!isFavourite && problems.isEmpty || isFavourite && favouriteProblems.isEmpty) {
            return tableView.frame.height - 2 * tableView.tableHeaderView!.frame.height
        } else {
            return 63
        }
    }
}
