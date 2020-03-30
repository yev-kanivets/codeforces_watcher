//
//  FiltersTableViewController.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 1/28/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import UIKit

class FiltersTableViewAdapter: NSObject, UITableViewDelegate, UITableViewDataSource {
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }

    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return Platform.allCases.count - 2
    }

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        return tableView.dequeueReusableCell(cellType: FilterTableViewCell.self).apply {
            let platform = Platform.allCases[indexPath.row]
            $0.bind(platform: platform) { isOn in
                store.dispatch(FilterChangeAction(platform: platform, isOn: isOn))
            }
        }
    }
}
