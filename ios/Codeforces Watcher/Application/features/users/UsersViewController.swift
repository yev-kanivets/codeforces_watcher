//
//  UsersViewController.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 4/10/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import UIKit
import common

class UsersViewController: UIViewControllerWithFab, StoreSubscriber {
    private let tableView = UITableView()
    private let tableAdapter = UsersTableViewAdapter()
    private let refreshControl = UIRefreshControl()
    
    override func viewWillAppear(_ animated: Bool) {
        store.subscribe(subscriber: self) { subscription in
            subscription.skipRepeats { oldState, newState in
                return KotlinBoolean(bool: oldState.users == newState.users)
            }.select { state in
                return state.users
            }
        }
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        store.unsubscribe(subscriber: self)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        setupView()
        setupTableView()
    }
    
    private func setupView() {
        view.backgroundColor = .white
        
        buildViewTree()
        setConstraints()
        setFabImage(named: "plusIcon")
    }
    
    private func buildViewTree() {
        view.addSubview(tableView)
    }
    
    private func setConstraints() {
        tableView.edgesToSuperview()
    }
    
    private func setupTableView() {
        tableView.run {
            $0.delegate = tableAdapter
            $0.dataSource = tableAdapter
            $0.separatorStyle = .none
            $0.refreshControl = refreshControl
        }

        [UserTableViewCell.self, NoItemsTableViewCell.self].forEach(tableView.registerForReuse(cellType:))

        refreshControl.run {
            $0.addTarget(self, action: #selector(refreshUsers), for: .valueChanged)
            $0.tintColor = Palette.colorPrimaryDark
        }
    }
    
    @objc func refreshUsers() {
        store.dispatch(action: UsersRequests.FetchUsers(source: Source.user))
        // add analytics
    }
    
    override func fabButtonTapped() {
        // open inputAccesoryView
    }
    
    func doNewState(state: Any) {
        let state = state as! UsersState
        
        if (state.status == .idle) {
            refreshControl.endRefreshing()
        }
        
        tableAdapter.users = state.users
        tableView.reloadData()
    }
}
