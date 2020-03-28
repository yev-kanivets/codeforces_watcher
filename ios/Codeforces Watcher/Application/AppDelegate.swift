//
//  AppDelegate.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 12/30/19.
//  Copyright © 2019 xorum.io. All rights reserved.
//

import UIKit
import ReSwift
import Firebase

let store = Store<AppState>(
    reducer: appReducer,
    state: AppState(),
    middleware: [appMiddleware]
)

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?
    let rootViewController = MainViewController()

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
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
        
        store.dispatch(ContestsRequests.FetchCodeforcesContests())

        FirebaseApp.configure()
        
        return true
    }
}
