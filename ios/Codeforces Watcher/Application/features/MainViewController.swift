//
//  NavigationView.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 1/15/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import Foundation
import UIKit
import MaterialComponents.MDCButton

class MainViewController: UITabBarController {
    private let controllers = [
        UsersViewController().apply(title: "Users", iconNamed: "usersIcon"),
        ContestsViewController().apply(title: "Contests", iconNamed: "contestsIcon"),
        UIViewController(),
        ActionsViewController().apply(title: "Actions", iconNamed: "actionsIcon"),
        ProblemsViewController().apply(title: "Problems", iconNamed: "problemsIcon")
    ]

    required init() {
        super.init(nibName: nil, bundle: nil)
        setupView()
    }

    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    private func setupView() {
        viewControllers = controllers.map { UINavigationController(rootViewController: $0) }
        if let tabBarArray = tabBar.items {
            tabBarArray[2].isEnabled = false
        }
    }
}

fileprivate extension UIViewController {

    func apply(title: String, iconNamed: String) -> UIViewController {
        let tabBarItem = UITabBarItem(title: title.localized, image: UIImage(named: iconNamed), selectedImage: nil)

        self.title = title.localized
        self.tabBarItem = tabBarItem

        return self
    }
}
