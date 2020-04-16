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
    
    private lazy var sortTextField = UITextField(frame: CGRect(x: 0, y: 0, width: self.navigationController!.navigationBar.frame.size.width, height: self.navigationController!.navigationBar.frame.size.height)).apply {
        $0.font = Font.textHeading
        $0.textColor = Palette.white
        $0.tintColor = .clear
    
        $0.textAlignment = .right
        $0.allowsEditingTextAttributes = false
    }
    private let pickerView = UIPickerView()
    private let pickerAdapter = UsersPickerViewAdapter()
    
    private lazy var bottomInputCardView = AddUserCardView(frame: CGRect(x: 0, y: 0, width: view.frame.width, height: 136)).apply {
        $0.isHidden = true
        $0.shouldMoveFurther = {
            self.addUser()
        }
    }
    
    private var users: [User] = []
    
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
        super.viewWillAppear(animated)
        
        store.subscribe(subscriber: self) { subscription in
            subscription.skipRepeats { oldState, newState in
                return KotlinBoolean(bool: oldState.users == newState.users && oldState.addUserState == newState.addUserState)
            }.select { state in
                return state.users
            }
        }
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        store.unsubscribe(subscriber: self)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        setupView()
        setupTableView()
        setupPickerView()
    }
    
    private func setupView() {
        view.backgroundColor = .white
        
        buildViewTree()
        setConstraints()
        setInteractions()
        setFabImage(named: "plusIcon")
    }
    
    private func setupPickerView() {
        pickerAdapter.run {
            $0.optionSelected = { position in
                let sortType = UsersState.SortTypeCompanion().getSortType(sortType: Int32(position))
                store.dispatch(action: UsersActions.Sort(sortType: sortType))
            }
            $0.options = ["default", "↓rating", "↑rating", "↓update", "↑update"]
        }
        pickerView.run {
            $0.backgroundColor = Palette.white
            $0.delegate = pickerAdapter
            $0.selectRow(Int(store.state.users.sortType.position), inComponent: 0, animated: false)
        }
        
        navigationItem.titleView = sortTextField
        
        let doneButton = UIBarButtonItem(title: "Done".localized, style: .plain, target: self, action: #selector(doneTapped))
        
        let toolBar = UIToolbar().apply {
            $0.sizeToFit()
            $0.setItems([doneButton], animated: false)
            $0.isUserInteractionEnabled = true
            $0.barTintColor = Palette.white
            $0.tintColor = Palette.colorPrimary
        }
        
        sortTextField.run {
            $0.inputView = pickerView
            $0.inputAccessoryView = toolBar
        }
    }
    
    @objc func doneTapped() {
        sortTextField.resignFirstResponder()
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
        PKHUD.sharedHUD.userInteractionOnUnderlyingViewsEnabled = false
        HUD.show(.progress, onView: UIApplication.shared.windows.last)
        
        let handle = bottomInputCardView.textField.text ?? ""
        store.dispatch(action: AddUserRequests.AddUser(handle: handle, language: "locale".localized))
    }
    
    private func showBottomInputView() {
        becomeFirstResponder()
        bottomInputCardView.run {
            $0.isHidden = false
            $0.textField.becomeFirstResponder()
        }
    }
    
    private func hideBottomInputView() {
        bottomInputCardView.textField.resignFirstResponder()
        resignFirstResponder()

        bottomInputCardView.textField.text = ""
    }
    
    private func sortUsers() {
        var sortedUsers = users
        
        switch(store.state.users.sortType) {
        case .default_:
            sortedUsers.reverse()
        case .ratingDown:
            sortedUsers.sort(by: {
                $0.rating?.intValue ?? Int.min > $1.rating?.intValue ?? Int.min
            })
        case .ratingUp:
            sortedUsers.sort(by: {
                $0.rating?.intValue ?? Int.min < $1.rating?.intValue ?? Int.min
            })
        case .updateDown:
            sortedUsers.sort(by: {
                $0.ratingChanges.last?.ratingUpdateTimeSeconds ?? Int64.min > $1.ratingChanges.last?.ratingUpdateTimeSeconds ?? Int64.min
            })
        case .updateUp:
            sortedUsers.sort(by: {
                $0.ratingChanges.last?.ratingUpdateTimeSeconds ?? Int64.min < $1.ratingChanges.last?.ratingUpdateTimeSeconds ?? Int64.min
            })
        default:
            break
        }
        
        tableAdapter.users = sortedUsers
        tableView.reloadData()
    }
    
    func doNewState(state: Any) {
        let state = state as! UsersState
        
        if (state.status == .idle) {
            refreshControl.endRefreshing()
        }
        
        users = state.users
        sortTextField.isHidden = users.isEmpty
        sortUsers()
        
        switch(store.state.addUserState.status) {
        case .done:
            HUD.hide(afterDelay: 0)
            
            hideBottomInputView()

            Analytics.logEvent("user_added", parameters: [:])
            store.dispatch(action: AddUserActions.ClearAddUserState())
        case .idle:
            HUD.hide(afterDelay: 0)
        default:
            break
        }
        
        let currentOption = pickerAdapter.options[Int(store.state.users.sortType.position)]
        sortTextField.text = "Sort by: \(currentOption) ▼"
    }
}
