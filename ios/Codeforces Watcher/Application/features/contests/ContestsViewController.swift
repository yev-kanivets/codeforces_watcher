//
//  ContestsViewController.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 1/15/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//
import Foundation
import UIKit
import ReSwift
import EventKit
import FirebaseAnalytics

class ContestsViewController: UIViewController, StoreSubscriber {
    private let contestsRulesView = ContestsRulesView()
    private let tableView = UITableView()
    private let tableAdapter = ContestsTableViewAdapter()
    private let refreshControl = UIRefreshControl()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        setupView()
        setupTableView()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        store.subscribe(self) { subcription in
            subcription.select { state in state.contests }
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
        
        navigationItem.rightBarButtonItem = UIBarButtonItem(image: UIImage(named: "filterIcon"), style: .plain, target: self, action: #selector(filterTapped))
        
        contestsRulesView.addGestureRecognizer(UITapGestureRecognizer(target: self, action:  #selector(self.contestsRulesTapped)))
    }
   
    @objc func filterTapped(sender: Any) {
        let filtersViewController = FiltersViewController()
        self.navigationController?.pushViewController(filtersViewController, animated: true)
    }
    
    @objc func contestsRulesTapped(sender: Any) {
        let webViewController = WebViewController().apply {
            $0.link = "https://codeforces.com/blog/entry/4088"
            $0.shareText = """
            Check Official Codeforces rules
            
            Shared through Codeforces Watcher. Find it on App Store.
            """
        }
        self.navigationController?.pushViewController(webViewController, animated: true)
    }
    
    func saveContestEvent(eventStore: EKEventStore, contest: ContestItem, completion: ((Bool, NSError?) -> Void)?) {
        let event = EKEvent(eventStore: eventStore)
        event.title = contest.name
        event.startDate = contest.startDate
        event.endDate = contest.endDate
        event.calendar = eventStore.defaultCalendarForNewEvents
        do {
            try eventStore.save(event, span: .thisEvent)
        } catch let e as NSError {
            completion?(false, e)
            return
        }
        completion?(true, nil)
    }
    
    func addEventToCalendar(_ contest: ContestItem, completion: ((Bool, NSError?) -> Void)?) {
        let eventStore = EKEventStore()
        
        if (EKEventStore.authorizationStatus(for: .event) != EKAuthorizationStatus.authorized) {
            eventStore.requestAccess(to: .event, completion: { (granted, error) in
                if (granted) && (error == nil) {
                    self.saveContestEvent(eventStore: eventStore, contest: contest, completion: { success, NSError in
                        completion?(success, NSError)
                    })
                } else {
                    completion?(false, error as NSError?)
                }
            })
        } else {
            saveContestEvent(eventStore: eventStore, contest: contest, completion: { success, NSError in
                completion?(success, NSError)
            })
        }
    }
    
    func showAlertWithOK(title: String, message: String) {
        let alertController = UIAlertController(title: title, message: message, preferredStyle: .alert)
        let okButton = UIAlertAction(title: "OK", style: .cancel)
        alertController.addAction(okButton)

        self.present(alertController, animated: true, completion: nil)
    }

    func setupTableView() {
        tableView.run {
            $0.delegate = tableAdapter
            $0.dataSource = tableAdapter
            $0.separatorStyle = .none
        }
    
        tableAdapter.onCalendarTap = { contest in
            self.addEventToCalendar(contest) { success, NSError in
                if (success) {
                    DispatchQueue.main.async{
                        Analytics.logEvent("add_contest_to_google_calendar", parameters: [:])
                        self.showAlertWithOK(title: contest.name, message: "Has been added to your calendar".localized)
                    }
                } else {
                    DispatchQueue.main.async{
                        self.showAlertWithOK(title: "Can't add contest to Calendar without permission".localized, message: "Enable it in Settings, please".localized)
                    }
                }
            }
        }
        
        [ContestTableViewCell.self, NoContestsTableViewCell.self].forEach(tableView.registerForReuse(cellType:))
        
        tableAdapter.onContestClick = { (contest) in
            let webViewController = WebViewController().apply {
                $0.link = contest.link
                $0.shareText = contest.shareText
                $0.openEventName = "contest_opened"
                $0.shareEventName = "contest_shared"
            }
            
            self.navigationController?.pushViewController(webViewController, animated: true)
        }
        
        tableView.refreshControl = refreshControl
        
        refreshControl.run {
            $0.addTarget(self, action: #selector(refreshContests(_:)), for: .valueChanged)
            $0.tintColor = Pallete.colorPrimaryDark
        }
    }
    
    @objc private func refreshContests(_ sender: Any) {
        Analytics.logEvent("contests_list_refresh", parameters: [:])
        fetchContests()
    }
    
    func newState(state: ContestsState) {
        if (state.status == ContestsState.Status.IDLE) {
            refreshControl.endRefreshing()
        }
        
        tableAdapter.contestItems = state.contestItems
            .filter { $0.phase == "BEFORE" }
            .filter {
                return state.filters[$0.site]!
            }
        
        tableAdapter.contestItems.sort(by: {
            $0.startDate < $1.startDate
        })
        tableView.reloadData()
    }

    func fetchContests() {
        store.dispatch(ContestsRequests.FetchCodeforcesContests())
    }

    func buildViewTree() {
        view.addSubview(tableView)
        
        tableView.tableHeaderView = contestsRulesView
    }

    func setConstraints() {
        tableView.run {
            $0.edgesToSuperview()
            $0.tableHeaderView?.widthToSuperview()
        }
        
        contestsRulesView.run {
            $0.setNeedsLayout()
            $0.layoutIfNeeded()
        }
    }
}
