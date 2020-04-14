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
import common
import FirebaseAnalytics

class ActionsViewController: UIViewControllerWithFab, StoreSubscriber {

    private let tableView = UITableView()
    private let tableAdapter = ActionsTableViewAdapter()
    private let refreshControl = UIRefreshControl()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        setupView()
        setupTableView()
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)

        store.subscribe(subscriber: self) { subscription in
            subscription.skipRepeats { oldState, newState in
                return KotlinBoolean(bool: oldState.actions == newState.actions)
            }.select { state in
                return state.actions
            }
        }
    }

    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        store.unsubscribe(subscriber: self)
    }

    private func setupView() {
        navigationController?.interactivePopGestureRecognizer?.isEnabled = false
        view.backgroundColor = .white

        buildViewTree()
        setConstraints()
        setFabImage(named: "shareImage")
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
            $0.tintColor = Palette.colorPrimaryDark
        }
    }

    func doNewState(state: Any) {
        let state = state as! ActionsState

        if (state.status == .idle) {
            refreshControl.endRefreshing()
        }

        tableAdapter.actions = state.actions
        tableView.reloadData()
    }
    
    override func fabButtonTapped() {
        let activityController = UIActivityViewController(activityItems: ["share_cw_message".localized], applicationActivities: nil).apply {
            $0.popoverPresentationController?.barButtonItem = navigationItem.rightBarButtonItem
        }
        
        present(activityController, animated: true)
    }

    @objc private func refreshActions(_ sender: Any) {
        Analytics.logEvent("actions_list_refresh", parameters: [:])
        fetchActions()
    }

    private func fetchActions() {
        store.dispatch(action: ActionsRequests.FetchActions(isInitializedByUser: true, language: "locale".localized))
    }
}
