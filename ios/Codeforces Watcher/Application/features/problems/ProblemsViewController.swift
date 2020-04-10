//
//  ProblemsViewController.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 1/17/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import UIKit
import common
import FirebaseAnalytics
import MaterialComponents.MaterialButtons

class ProblemsViewController: UIViewController, StoreSubscriber, UISearchResultsUpdating {
    
    private let tableView = UITableView()
    private let tableAdapter = ProblemsTableViewAdapter()
    private let refreshControl = UIRefreshControl()
    private let searchController = UISearchController(searchResultsController: nil)
    private let fabButton = MDCFloatingButton().apply {
        $0.backgroundColor = Palette.colorAccent
        $0.tintColor = Palette.white
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        setupView()
        setupTableView()
        setupSearchView()
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        fabButton.isHidden = false
        store.subscribe(subscriber: self) { subscription in
            subscription.skipRepeats { oldState, newState in
                return KotlinBoolean(bool: oldState.problems == newState.problems)
            }.select { state in
                return state.problems
            }
        }
    }

    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        fabButton.isHidden = true
        store.unsubscribe(subscriber: self)
    }

    private func setupView() {
        view.backgroundColor = .white

        buildViewTree()
        setConstraints()
        setInteractions()
        updateFabButton(store.state.problems.isFavourite)
    }
    
    private func buildViewTree() {
        tabBarController?.view.addSubview(fabButton)
        [tableView].forEach(view.addSubview)
    }

    private func setConstraints() {
        edgesForExtendedLayout = []
        
        tableView.edgesToSuperview()
        fabButton.run {
            $0.bottomToSuperview(offset: -16)
            $0.centerXToSuperview()
        }
    }

    private func setupTableView() {
        tableView.run {
            $0.delegate = tableAdapter
            $0.dataSource = tableAdapter
            $0.separatorStyle = .none
        }

        [ProblemTableViewCell.self, NoProblemsTableViewCell.self].forEach(tableView.registerForReuse(cellType:))

        tableAdapter.onProblemClick = { (link, shareText) in
            let webViewController = WebViewController().apply {
                $0.link = link
                $0.shareText = shareText
                $0.openEventName = "problem_opened"
                $0.shareEventName = "problem_shared"
            }
            self.searchController.isActive = false
            self.navigationController?.pushViewController(webViewController, animated: true)
        }

        tableView.refreshControl = refreshControl

        refreshControl.run {
            $0.addTarget(self, action: #selector(refreshProblems(_:)), for: .valueChanged)
            $0.tintColor = Palette.colorPrimaryDark
        }
    }
    
    private func setInteractions() {
        fabButton.addTarget(self, action: #selector(fabButtonTapped), for: .touchUpInside)
    }
    
    @objc func fabButtonTapped() {
        var isFavourite = store.state.problems.isFavourite
        
        isFavourite = !isFavourite
        
        store.dispatch(action: ProblemsActions.ChangeTypeProblems(isFavourite: isFavourite))
        
        updateFabButton(isFavourite)
    }

    private func updateFabButton(_ isFavourite: Bool) {
        fabButton.setImage(UIImage(named: isFavourite ? "infinityIcon" : "starIcon")?.withRenderingMode(.alwaysTemplate), for: .normal)
    }

    private func setupSearchView() {
        searchController.run {
            $0.searchResultsUpdater = self
            $0.obscuresBackgroundDuringPresentation = false
            $0.hidesNavigationBarDuringPresentation = false

            $0.searchBar.run {
                $0.placeholder = "Search for problems...".localized
                $0.returnKeyType = .done
                $0.tintColor = .darkGray
                $0.barStyle = .default
                $0.searchBarStyle = .minimal
                $0.backgroundColor = .white
            }
        }

        tableView.tableHeaderView = searchController.searchBar
    }

    func updateSearchResults(for searchController: UISearchController) {
        guard let text = searchController.searchBar.text?.lowercased() else { return }

        var filteredProblems: [Problem] = []

        for problem in store.state.problems.problems {
            if (problem.contestName.lowercased().contains(text) || problem.enName.lowercased().contains(text) ||
                problem.ruName.lowercased().contains(text)) {
                filteredProblems.append(problem)
            }
        }

        tableAdapter.problems = text.isEmpty ? store.state.problems.problems : filteredProblems
        tableAdapter.favouriteProblems = tableAdapter.problems.filter { $0.isFavourite }
        
        tableView.reloadData()
    }

    func doNewState(state: Any) {
        let state = state as! ProblemsState

        if (state.status == ProblemsState.Status.idle) {
            refreshControl.endRefreshing()
        }

        tableAdapter.problems = state.problems
        tableAdapter.favouriteProblems = state.problems.filter { $0.isFavourite }
        
        updateSearchResults(for: searchController)
        tableView.reloadData()
    }

    @objc private func refreshProblems(_ sender: Any) {
        Analytics.logEvent("problems_list_refresh", parameters: [:])
        fetchProblems()
    }

    private func fetchProblems() {
        store.dispatch(action: ProblemsRequests.FetchProblems(isInitializedByUser: true))
    }
}
