//
//  UserViewController.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 4/16/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import UIKit
import common
import Charts

class UserViewController: UIViewController {
    private let userImage = CircleImageView()
    private let rankLabel = BodyLabel()
    private let nameLabel = BodyLabel()
    private let currentRatingLabel = BodyLabel()
    private let maxRatingLabel = BodyLabel()
    
    private let ratingChangesLabel = HeadingLabel().apply {
        $0.text = "Rating changes".localized
    }
    
    private let lineChartView = LineChartView()
    
    private let user: User
    
    init(_ user: User) {
        self.user = user
        super.init(nibName: nil, bundle: nil)
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        setupView()
    }
    
    private func setupView() {
        title = user.handle
        view.backgroundColor = Palette.white
        
        navigationItem.rightBarButtonItem = UIBarButtonItem(image: UIImage(named: "removeIcon"), style: .plain, target: self, action: #selector(removeTapped))
        
        buildViewTree()
        setConstraints()
        setData()
    }
    
    @objc func removeTapped() {
        store.dispatch(action: UsersRequests.DeleteUser(user: user))
        navigationController?.popViewController(animated: true)
    }
    
    private func buildViewTree() {
        [userImage, rankLabel, nameLabel, currentRatingLabel, maxRatingLabel, ratingChangesLabel, lineChartView].forEach(view.addSubview)
    }
    
    private func setConstraints() {
        userImage.run {
            $0.topToSuperview(offset: 16)
            $0.leadingToSuperview(offset: 16)
            $0.width(80)
            $0.height(80)
        }
        
        rankLabel.run {
            $0.leadingToTrailing(of: userImage, offset: 20)
            $0.top(to: userImage, offset: 4)
        }
        
        nameLabel.run {
            $0.topToBottom(of: rankLabel, offset: 2)
            $0.leading(to: rankLabel)
        }
        
        currentRatingLabel.run {
            $0.topToBottom(of: nameLabel, offset: 2)
            $0.leading(to: rankLabel)
        }
        
        maxRatingLabel.run {
            $0.topToBottom(of: currentRatingLabel, offset: 2)
            $0.leading(to: rankLabel)
        }
        
        ratingChangesLabel.run {
            $0.leading(to: userImage)
            $0.topToBottom(of: userImage, offset: 16)
        }
        
        lineChartView.run {
            $0.leadingToSuperview(offset: 16)
            $0.trailingToSuperview(offset: 16)
            $0.topToBottom(of: ratingChangesLabel, offset: 16)
            $0.bottomToSuperview(offset: 16)
        }
    }
    
    private func setData() {
        let avatar = LinkValidatorKt.avatar(avatarLink: user.avatar)
        userImage.sd_setImage(with: URL(string: avatar), placeholderImage: noImage)
        
        let none = "None".localized
        
        rankLabel.text = "Rank".localizedFormat(args: user.rank ?? none)
        nameLabel.text = "Name".localizedFormat(args: user.firstName ?? "", user.lastName ?? none)
        currentRatingLabel.text = "Current rating".localizedFormat(args: user.rating ?? none)
        maxRatingLabel.text = "Max rating".localizedFormat(args: user.maxRating ?? none)
    }
}
