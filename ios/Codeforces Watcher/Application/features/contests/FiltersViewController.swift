//
//  FilterViewController.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 1/28/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import Foundation
import UIKit

class FiltersViewController: UIViewController {
    private let tableView = UITableView()
    private let tableAdapter = FiltersTableViewAdapter()

    private let switchView = UISwitch()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        setupView()
        setupTableView()
    }
    
    func setupView() {
        view.backgroundColor = Pallete.white
        self.title = "Filter".localized
        
        buildViewTree()
        setConstraints()
    }
    
    func setupTableView() {
        tableView.run {
            $0.delegate = tableAdapter
            $0.dataSource = tableAdapter
            $0.separatorStyle = .none
            $0.rowHeight = 58
        }
        
        tableView.registerForReuse(cellType: FilterTableViewCell.self)
    }
    
    func buildViewTree() {
        view.addSubview(tableView)
    }
    
    func setConstraints() {
        tableView.edgesToSuperview()
    }
}
