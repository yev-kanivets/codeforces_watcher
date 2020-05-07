//
//  FeedbackController.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 5/6/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import Foundation
import StoreKit
import common

class FeedbackController: IFeedbackController {

    private var KEY_CURRENT_SHOWING_ITEM: String {
        get { "key_current_showing_item" }
    }

    private var KEY_COUNT_SCREEN_OPENING: String {
        get { "key_count_app_opening"}
    }

    private var KEY_START_TIME_SHOWING: String {
        get { "key_first_time_shown" }
    }

    private var KEY_IS_NEEDED_AT_ALL: String {
        get { "key_is_needed_to_show" }
    }

    private var KEY_IS_LOCKOUT_PERIOD: String {
        get { "key_is_lockout_period" }
    }
    
    override var currentShowingItem: Int32 {
        get { UserDefaults.standard.object(forKey: KEY_CURRENT_SHOWING_ITEM) as? Int32 ?? 0 }
        set(value) { UserDefaults.standard.set(value, forKey: KEY_CURRENT_SHOWING_ITEM) }
    }
    
    override var countScreenOpening: Int32 {
        get { UserDefaults.standard.object(forKey: KEY_COUNT_SCREEN_OPENING) as? Int32 ?? 0 }
        set(value) { UserDefaults.standard.set(Int(value), forKey: KEY_COUNT_SCREEN_OPENING) }
    }

    override var startTimeWhenShown: Int64 {
        get { UserDefaults.standard.object(forKey: KEY_START_TIME_SHOWING) as? Int64 ?? 0 }
        set(value) { UserDefaults.standard.set(value, forKey: KEY_START_TIME_SHOWING) }
    }

    override var isNeededAtAll: Bool {
        get { UserDefaults.standard.object(forKey: KEY_IS_NEEDED_AT_ALL) as? Bool ?? true }
        set(value) { UserDefaults.standard.set(value, forKey: KEY_IS_NEEDED_AT_ALL) }
    }

    override var isLockoutPeriod: Bool {
        get { UserDefaults.standard.bool(forKey: KEY_IS_LOCKOUT_PERIOD) }
        set(value) { UserDefaults.standard.set(value, forKey: KEY_IS_LOCKOUT_PERIOD) }
    }
    
    override func buildEnjoyingItem() -> FeedbackItem {
        FeedbackItem(
            textPositiveButton: "yes".localized,
            textNegativeButton: "not_really".localized,
            textTitle: "rate_us_first_title".localized,
            positiveButtonClick: { self.currentShowingItem = 2 },
            negativeButtonClick: { self.currentShowingItem = 1 },
            neutralButtonClick: turnOnLockoutPeriod
        )
    }

    override func buildRateItem() -> FeedbackItem {
        FeedbackItem(
            textPositiveButton: "yes".localized,
            textNegativeButton: "no_thanks".localized,
            textTitle: "rate_us_third_title".localized,
            positiveButtonClick: { self.showAppStore(); self.isNeededAtAll = false },
            negativeButtonClick: turnOnLockoutPeriod,
            neutralButtonClick: turnOnLockoutPeriod
        )
    }
    
    override func buildEmailItem() -> FeedbackItem {
        FeedbackItem(
            textPositiveButton: "yes".localized,
            textNegativeButton: "no_thanks".localized,
            textTitle: "rate_us_second_title".localized,
            positiveButtonClick: { self.showEmailApp() },
            negativeButtonClick: turnOnLockoutPeriod,
            neutralButtonClick: turnOnLockoutPeriod
        )
    }
    
    override func showEmailApp() {
        if let url = URL(string: "mailto:support@xorum.io?subject=Feedback about Codeforces WatchR App".addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)!) {
            UIApplication.shared.open(url)
        }
    }

    override func showAppStore() {
        SKStoreReviewController.requestReview()
    }

    override func currentTimeMillis() -> Int64 {
        return Int64(Date().timeIntervalSince1970 * 1000)
    }
}
