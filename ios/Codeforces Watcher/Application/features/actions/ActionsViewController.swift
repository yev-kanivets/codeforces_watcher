//
//  ActionsViewController.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 12/31/19.
//  Copyright © 2019 xorum.io. All rights reserved.
//

import UIKit
import TinyConstraints
import WebKit
import ReSwift
import FirebaseAnalytics

class ActionsViewController: UIViewController, StoreSubscriber {
    
    private let tableView = UITableView()
    private let tableAdapter = ActionsTableViewAdapter()
    private let refreshControl = UIRefreshControl()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        setupView()
        setupTableView()
        fetchActions()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        store.subscribe(self) { subcription in
            subcription.select { state in state.actions }
        }
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        store.unsubscribe(self)
    }
    
    func newState(state: ActionsState) {
        if (state.status == .IDLE) {
            refreshControl.endRefreshing()
        }
        
        tableAdapter.actionItems = state.actionItems
        tableView.reloadData()
    }
    
    func setupView() {
        self.navigationController?.interactivePopGestureRecognizer?.isEnabled = false
        view.backgroundColor = .white
        
        buildViewTree()
        setConstraints()
    }
    
    @objc private func refreshActions(_ sender: Any) {
        Analytics.logEvent("actions_list_refresh", parameters: [:])
        fetchActions()
    }
    
    func setupTableView() {
        tableView.run {
            $0.delegate = tableAdapter
            $0.dataSource = tableAdapter
            $0.separatorStyle = .none
        }
        
        [CommentTableViewCell.self, BlogEntryTableViewCell.self, NoActionsTableViewCell.self].forEach(tableView.registerForReuse(cellType:))
        
        tableAdapter.onActionClick = { (link, shareText) in
            let webViewController = WebViewController().apply {
                $0.link = link
                $0.shareText = shareText
                $0.openEventName = "action_opened"
                $0.shareEventName = "action_share_comment"
            }
            self.navigationController?.pushViewController(webViewController, animated: true)
        }
        
        tableView.refreshControl = refreshControl
        
        refreshControl.run {
            $0.addTarget(self, action: #selector(refreshActions(_:)), for: .valueChanged)
            $0.tintColor = Pallete.colorPrimaryDark
        }
    }

    func fetchActions() {
        store.dispatch(ActionsRequests.FetchActions())
    }

    func buildViewTree() {
        view.addSubview(tableView)
    }

    func setConstraints() {
        tableView.edgesToSuperview()
    }
}
