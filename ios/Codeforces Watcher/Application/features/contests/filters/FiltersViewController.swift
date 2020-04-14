//
//  FilterViewController.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 1/28/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import Foundation
import UIKit
import common

class FiltersViewController: UIViewController {
    
    private let tableView = UITableView()
    private let tableAdapter = FiltersTableViewAdapter()

    override func viewDidLoad() {
        super.viewDidLoad()

        setupView()
        setupTableView()
    }

    private func setupView() {
        view.backgroundColor = Palette.white
        title = "Filter".localized

        buildViewTree()
        setConstraints()
    }

    private func setupTableView() {
        tableView.run {
            $0.delegate = tableAdapter
            $0.dataSource = tableAdapter
            $0.separatorStyle = .none
            $0.rowHeight = 58
        }

        tableView.registerForReuse(cellType: FilterTableViewCell.self)

        let filters = store.state.contests.filters

        tableAdapter.filterItems = [
            FilterItem(title: "Codeforces", platform: Platform.codeforces, isOn: filters.contains(Platform.codeforces)),
            FilterItem(title: "Codeforces Gym", platform: Platform.codeforcesGym, isOn: filters.contains(Platform.codeforcesGym)),
            FilterItem(title: "AtCoder", platform: Platform.atcoder, isOn: filters.contains(Platform.atcoder)),
            FilterItem(title: "LeetCode", platform: Platform.leetcode, isOn: filters.contains(Platform.leetcode)),
            FilterItem(title: "TopCoder", platform: Platform.topcoder, isOn: filters.contains(Platform.topcoder)),
            FilterItem(title: "CS Academy", platform: Platform.csAcademy, isOn: filters.contains(Platform.csAcademy)),
            FilterItem(title: "CodeChef", platform: Platform.codechef, isOn: filters.contains(Platform.codechef)),
            FilterItem(title: "HackerRank", platform: Platform.hackerrank, isOn: filters.contains(Platform.hackerrank)),
            FilterItem(title: "HackerEarth", platform: Platform.hackerearth, isOn: filters.contains(Platform.hackerearth)),
            FilterItem(title: "Kick Start", platform: Platform.kickStart, isOn: filters.contains(Platform.kickStart))
        ]

        tableView.reloadData()
    }

    private func buildViewTree() {
        view.addSubview(tableView)
    }

    private func setConstraints() {
        tableView.edgesToSuperview()
    }
}
