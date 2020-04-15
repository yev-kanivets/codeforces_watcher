//
//  UserTableViewCell.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 4/14/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import UIKit
import common

class UserTableViewCell: UITableViewCell {
    private let cardView = CardView()
    
    private let userImage = CircleImageView()
    private let handleLabel = HeadingLabel()
    private let ratingUpdateDateLabel = SubheadingBigLabel()
    
    private let ratingLabel = HeadingLabel()
    private let arrowView = UIView()
    private let ratingUpdateLabel = SubheadingBigLabel()
    
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        setupView()
    }

    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        setupView()
    }
    
    private func setupView() {
        selectionStyle = .none
        
        buildViewTree()
        setConstraints()
    }
    
    private func buildViewTree() {
        addSubview(cardView)
        
        [userImage, handleLabel, ratingUpdateDateLabel, ratingLabel, arrowView, ratingUpdateLabel].forEach(cardView.addSubview)
    }
    
    private func setConstraints() {
        cardView.edgesToSuperview(insets: UIEdgeInsets(top: 8, left: 8, bottom: 0, right: 8))
        
        userImage.run {
            $0.leadingToSuperview(offset: 8)
            $0.height(36)
            $0.width(36)
            $0.centerYToSuperview()
        }
        
        handleLabel.run {
            $0.topToSuperview(offset: 8)
            $0.leadingToTrailing(of: userImage, offset: 8)
            $0.trailingToLeading(of: ratingLabel, offset: 4)
        }
        
        ratingUpdateDateLabel.run {
            $0.topToBottom(of: handleLabel, offset: 4)
            $0.leading(to: handleLabel)
            $0.bottomToSuperview(offset: -8)
        }
        
        ratingLabel.run {
            $0.topToSuperview(offset: 8)
            $0.trailingToSuperview(offset: 8)
        }
    }
    
    func bind(_ user: User) {
        
    }
}
