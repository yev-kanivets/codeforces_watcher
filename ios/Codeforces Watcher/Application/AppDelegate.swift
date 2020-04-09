//
//  AppDelegate.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 12/30/19.
//  Copyright © 2019 xorum.io. All rights reserved.
//

import UIKit
import Firebase
import ReSwift
import common

let newStore = AppStoreKt.store

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?
    let rootViewController = MainViewController()

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        initDatabase()
        initSettings()
        initToastHandler()
        
        AppStoreKt.databaseController.onAppCreated()
        AppStoreKt.persistenceController.onAppCreated()
        
        FirebaseApp.configure()
        
        initAppStyle()
        fetchData()
        
        return true
    }

    private func initDatabase() {
        DatabaseKt.doInitDatabase()
    }
    
    private func initSettings() {
        SettingsKt.settings = Prefs()
    }
    
    private func initToastHandler() {
        ToastMiddlewareKt.toastHandlers.add(IOSToastHandler())
    }
    
    private func fetchData() {
        newStore.dispatch(action: ActionsRequests.FetchActions(isInitializedByUser: false, language: "locale".localized))
        newStore.dispatch(action: ContestsRequests.FetchContests(isInitiatedByUser: false))
    }
    
    private func initAppStyle() {
        UINavigationBar.appearance().run {
            $0.isTranslucent = false
            $0.barTintColor = Pallete.colorPrimary
            $0.tintColor = Pallete.white
            $0.titleTextAttributes = [NSAttributedString.Key.foregroundColor: Pallete.white,
                                      NSAttributedString.Key.font: Font.textPageTitle]
        }
        
        UITabBar.appearance().run {
            $0.isTranslucent = false
        }

        window = UIWindow(frame: UIScreen.main.bounds)
        window!.rootViewController = rootViewController
        window!.makeKeyAndVisible()
        
        if #available(iOS 13.0, *) {
            window?.overrideUserInterfaceStyle = .light
        }
    }
}
