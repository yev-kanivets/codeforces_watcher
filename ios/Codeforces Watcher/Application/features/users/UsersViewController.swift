//
//  UsersViewController.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 4/10/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import UIKit
import common
import FirebaseAnalytics
import PKHUD

class UsersViewController: UIViewControllerWithFab, StoreSubscriber {
    private let tableView = UITableView()
    private let tableAdapter = UsersTableViewAdapter()
    private let refreshControl = UIRefreshControl()
    private lazy var bottomInputCardView = AddUserCardView(frame: CGRect(x: 0, y: 0, width: view.frame.width, height: 136)).apply {
        $0.shouldMoveFurther = {
            self.addUser()
        }
    }
    
    override var inputAccessoryView: UIView? {
        get {
            return bottomInputCardView
        }
    }
    
    override var canBecomeFirstResponder: Bool {
        return true
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        resignFirstResponder()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        store.subscribe(subscriber: self) { subscription in
            subscription.skipRepeats { oldState, newState in
                return KotlinBoolean(bool: oldState.users == newState.users)
            }.select { state in
                return state.users
            }
        }
        
        store.subscribe(subscriber: self) { subscription in
            subscription.skipRepeats { oldState, newState in
                return KotlinBoolean(bool: oldState.addUserState == newState.addUserState)
            }.select { state in
                return state.addUserState
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
        setInteractions()
        setFabImage(named: "plusIcon")
    }
    
    private func buildViewTree() {
        view.addSubview(tableView)
    }
    
    private func setConstraints() {
        tableView.edgesToSuperview()
    }
    
    private func setInteractions() {
        view.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(didTapOutside)).apply {
            $0.cancelsTouchesInView = false
        })
    }
    
    @objc func didTapOutside(_ tapGestureRecognizer: UITapGestureRecognizer) {
        hideBottomInputView()
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
        store.dispatch(action: UsersRequests.FetchUsers(source: Source.user, language: "locale".localized))
        Analytics.logEvent("users_list_refresh", parameters: [:])
    }
    
    override func fabButtonTapped() {
        showBottomInputView()
    }
    
    private func addUser() {
        PKHUD.sharedHUD.userInteractionOnUnderlyingViewsEnabled = true
        
        HUD.show(.progress, onView: UIApplication.shared.windows.last)
        
        let handle = bottomInputCardView.textField.text ?? ""
        store.dispatch(action: AddUserRequests.AddUser(handle: handle, language: "locale".localized))
    }
    
    private func showBottomInputView() {
        becomeFirstResponder()
        bottomInputCardView.textField.becomeFirstResponder()
    }
    
    private func hideBottomInputView() {
        bottomInputCardView.textField.resignFirstResponder()
        resignFirstResponder()

        bottomInputCardView.textField.text = ""
    }
    
    func doNewState(state: Any) {
        if let state = state as? UsersState {
            if (state.status == .idle) {
                refreshControl.endRefreshing()
            }
            
            tableAdapter.users = state.users
            tableView.reloadData()
        }
        
        if let state = state as? AddUserState {
            if (state.status != .pending) {
                HUD.hide(afterDelay: 0)
            }
            
            if (state.status == .done) {
                hideBottomInputView()
                
                tableAdapter.users = store.state.users.users
                tableView.reloadData()
                
                Analytics.logEvent("user_added", parameters: [:])
                store.dispatch(action: AddUserActions.ClearAddUserState())
            }
        }
    }
}
