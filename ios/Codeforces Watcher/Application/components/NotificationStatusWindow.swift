//
//  NotificationStatusWindow.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 1/22/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//
import Foundation
import UIKit
import ReSwift
import TinyConstraints

class NotificationStatusWindow: UIWindow {

    let controller = NotificationStatusController()

    override init(frame: CGRect) {
        super.init(frame: CGRect.zero)
        windowLevel = UIWindow.Level.statusBar
        rootViewController = controller
        rootViewController?.view.bounds = self.bounds
    }

    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }
    
    func fireNotification(message: String) {
        frame = CGRect(x: 0, y: 0, width: 180, height: 40)
        
        let screenSize = UIScreen.main.bounds
        center = CGPoint(x: screenSize.midX, y: screenSize.maxY - 100)
        
        controller.run {
            $0.label.text = message
            $0.statusView.backgroundColor = Pallete.red
        }
        
        isHidden = false
        makeKeyAndVisible()
        alpha = 0
        
        UIView.animate(withDuration: 0.3) {
            self.alpha = 0.99
        }
        
        DispatchQueue.main.asyncAfter(deadline: .now() + 3.5) {
            UIView.animate(withDuration: 1.0, animations: {
                self.alpha = 0
            }, completion: { (_) in
                self.isHidden = true
            })
        }
    }
}

class NotificationStatusController: UIViewController {
    let statusView = UIView(frame: CGRect.zero)
    let visualView = UIVisualEffectView(effect: UIBlurEffect(style: .dark)).apply {
        $0.layer.cornerRadius = 8
        $0.clipsToBounds = true
    }
    
    let label = UILabel(frame: CGRect.zero).apply {
        $0.font = Font.textBody
        $0.backgroundColor = .clear
        $0.textAlignment = .center
        $0.textColor = .white
        $0.numberOfLines = 0
    }
    
    init() {
        super.init(nibName: nil, bundle: nil)
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        setupView()
    }
    
    func setupView() {
        view.run {
            $0.backgroundColor = .clear
            $0.layer.cornerRadius = 8
            $0.clipsToBounds = true
        }
        
        buildViewTree()
        setConstraints()
    }
    
    func buildViewTree() {
        [visualView, label, statusView].forEach(view.addSubview)
    }
    
    func setConstraints() {
        label.edges(to: view)
        visualView.edges(to: view)
        
        statusView.run {
            $0.leading(to: view)
            $0.top(to: view)
            $0.bottom(to: view)
            $0.width(6)
        }
    }
}
