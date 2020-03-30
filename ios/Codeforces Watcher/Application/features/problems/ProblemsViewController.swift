//
//  ProblemsViewController.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 1/17/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import UIKit
import ReSwift
import FirebaseAnalytics

class ProblemsViewController: UIViewController, StoreSubscriber, UISearchResultsUpdating {

    private let tableView = UITableView()
    private let tableAdapter = ProblemsTableViewAdapter()
    private let refreshControl = UIRefreshControl()
    private let searchController = UISearchController(searchResultsController: nil)

    override func viewDidLoad() {
        super.viewDidLoad()

        setupView()
        setupTableView()
        setupSearchView()
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        store.subscribe(self) { subcription in
            subcription.select { state in state.problems }
        }
    }

    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        store.unsubscribe(self)
    }

    func setupView() {
        view.backgroundColor = .white

        buildViewTree()
        setConstraints()
    }

    func setupTableView() {
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
            $0.tintColor = Pallete.colorPrimaryDark
        }
    }

    func setupSearchView() {
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
        guard let text = searchController.searchBar.text else { return }

        var filteredProblemItems: [ProblemItem] = []

        for problem in store.state.problems.problemItems {
            if (problem.round.lowercased().contains(text.lowercased()) || problem.englishTitle.lowercased().contains(text.lowercased()) ||
                problem.russianTitle.lowercased().contains(text.lowercased())) {
                filteredProblemItems.append(problem)
            }
        }

        tableAdapter.problemItems = text.isEmpty ? store.state.problems.problemItems : filteredProblemItems
        tableView.reloadData()
    }

    func newState(state: ProblemsState) {
        if (state.status == ProblemsState.Status.IDLE) {
            refreshControl.endRefreshing()
        }

        tableAdapter.problemItems = state.problemItems
        updateSearchResults(for: searchController)
        tableView.reloadData()
    }

    @objc private func refreshProblems(_ sender: Any) {
        Analytics.logEvent("problems_list_refresh", parameters: [:])
        fetchProblems()
    }

    func fetchProblems() {
        store.dispatch(ProblemsRequests.FetchProblems())
    }

    func buildViewTree() {
        view.addSubview(tableView)
    }

    func setConstraints() {
        tableView.edgesToSuperview()
    }
}
