//
//  FeedbackController.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 5/6/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import Foundation
import StoreKit

class FeedbackController {

    private var KEY_CURRENT_SHOWING_ITEM: String {
        get { "key_current_showing_item" }
    }

    private var KEY_COUNT_SCREEN_OPENING: String {
        get { "key_count_app_opening" }
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

    private var currentShowingItem: Int {
        get { UserDefaults.standard.integer(forKey: KEY_CURRENT_SHOWING_ITEM) }
        set(value) { UserDefaults.standard.set(value, forKey: KEY_CURRENT_SHOWING_ITEM) }
    }

    private var countScreenOpening: Int {
        get { UserDefaults.standard.integer(forKey: KEY_COUNT_SCREEN_OPENING) }
        set(value) { UserDefaults.standard.set(value, forKey: KEY_COUNT_SCREEN_OPENING) }
    }

    private var startTimeWhenShown: Int64 {
        get { UserDefaults.standard.object(forKey: KEY_START_TIME_SHOWING) as? Int64 ?? 0 }
        set(value) { UserDefaults.standard.set(value, forKey: KEY_START_TIME_SHOWING) }
    }

    private var isNeededAtAll: Bool {
        get { UserDefaults.standard.object(forKey: KEY_IS_NEEDED_AT_ALL) as? Bool ?? true }
        set(value) { UserDefaults.standard.set(value, forKey: KEY_IS_NEEDED_AT_ALL) }
    }

    private var isLockoutPeriod: Bool {
        get { UserDefaults.standard.bool(forKey: KEY_IS_LOCKOUT_PERIOD) }
        set(value) { UserDefaults.standard.set(value, forKey: KEY_IS_LOCKOUT_PERIOD) }
    }

    var feedbackItem: FeedbackItem {
        get {
            switch currentShowingItem {
            case 0: return buildFirst()
            case 1: isNeededAtAll = false; return buildSecond()
            case 2: return buildThird()
            default:
                fatalError()
            }
        }
    }

    private func buildFirst() -> FeedbackItem {
        FeedbackItem(
            textPositiveButton: "yes".localized,
            textNegativeButton: "not_really".localized,
            textTitle: "rate_us_first_title".localized,
            positiveButtonClick: { self.currentShowingItem = 2 },
            negativeButtonClick: { self.currentShowingItem = 1 },
            neutralButtonClick: turnOnLockoutPeriod
        )
    }

    private func buildSecond() -> FeedbackItem {
        FeedbackItem(
            textPositiveButton: "yes".localized,
            textNegativeButton: "no_thanks".localized,
            textTitle: "rate_us_second_title".localized,
            positiveButtonClick: { self.showEmailApp() },
            negativeButtonClick: turnOnLockoutPeriod,
            neutralButtonClick: turnOnLockoutPeriod
        )
    }

    private func buildThird() -> FeedbackItem {
        FeedbackItem(
            textPositiveButton: "yes".localized,
            textNegativeButton: "no_thanks".localized,
            textTitle: "rate_us_third_title".localized,
            positiveButtonClick: { self.showAppStore(); self.isNeededAtAll = false },
            negativeButtonClick: turnOnLockoutPeriod,
            neutralButtonClick: turnOnLockoutPeriod
        )
    }
    
    private func showEmailApp() {
        if let url = URL(string: "mailto:support@xorum.io?subject=Feedback about Codeforces WatchR App".addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)!) {
            UIApplication.shared.open(url)
        }
    }

    private func showAppStore() {
        SKStoreReviewController.requestReview()
    }

    private func turnOnLockoutPeriod() {
        isLockoutPeriod = true
        currentShowingItem = 0

        startTimeWhenShown = currentTimeMillis()
        countScreenOpening = 0
    }

    func shouldShowFeedbackCell() -> Bool {
        if startTimeWhenShown == 0 { startTimeWhenShown = currentTimeMillis() }

        let timeInHours = calculateTimeInHours(startTimeInMillis: startTimeWhenShown, endTimeInMillis: currentTimeMillis())
        updateLockoutPeriod(timeInHours: timeInHours)
        
        return isNeededAtAll && !isLockoutPeriod &&
            (countScreenOpening >= 10 && timeInHours >= 72 || countScreenOpening >= 1)
    }

    private func updateLockoutPeriod(timeInHours: Int) {
        if timeInHours >= 168 && isLockoutPeriod  { isLockoutPeriod = false }
    }

    private func calculateTimeInHours(startTimeInMillis: Int64, endTimeInMillis: Int64) -> Int {
        fromMillisToHours(timeInMillis: endTimeInMillis - startTimeInMillis)
    }

    private func fromMillisToHours(timeInMillis: Int64) -> Int { Int(timeInMillis / 1000 / 60 / 60) }

    private func currentTimeMillis() -> Int64 { Int64(Date().timeIntervalSince1970 * 1000) }

    func updateCountOpeningScreen() {
        countScreenOpening += 1
    }
}

struct FeedbackItem {
    let textPositiveButton: String
    let textNegativeButton: String
    let textTitle: String
    let positiveButtonClick: () -> ()
    let negativeButtonClick: () -> ()
    let neutralButtonClick: () -> ()
}
