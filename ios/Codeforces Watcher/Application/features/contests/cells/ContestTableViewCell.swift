//
//  ContestTableViewCell.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 1/15/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import UIKit
import EventKit

class ContestTableViewCell: UITableViewCell {
    private var onCalendarTap: (() -> ())!
    private let cardView = CardView()
    
    private var logoView = UIImageView().apply {
        $0.layer.run {
            $0.cornerRadius = 18
            $0.masksToBounds = true
            $0.borderWidth = 1
            $0.borderColor = Pallete.colorPrimary.cgColor
        }
    }
    
    private let nameLabel = UILabel().apply {
        $0.font = Font.textHeading
        $0.textColor = Pallete.black
    }
    
    private let timeLabel = UILabel().apply {
        $0.font = Font.textSubheadingBig
        $0.textColor = Pallete.grey
    }
    
    private let calendarAddIcon = UIImageView(image: UIImage(named: "calendarAddIcon"))

    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        setupView()
    }

    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        setupView()
    }
    
    @objc func calendarIconTapped(recognizer: UITapGestureRecognizer) {
        onCalendarTap()
    }

    private func setupView() {
        self.selectionStyle = .none
        
        calendarAddIcon.run {
            $0.isUserInteractionEnabled = true
            $0.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(calendarIconTapped)))
        }
        
        buildViewTree()
        setConstraints()
    }

    private func buildViewTree() {
        contentView.addSubview(cardView)
        
        [logoView, nameLabel, timeLabel, calendarAddIcon].forEach(cardView.addSubview)
    }

    private func setConstraints() {
        cardView.edgesToSuperview(insets: UIEdgeInsets(top: 8, left: 8, bottom: 0, right: 8))
        
        logoView.run {
            $0.leadingToSuperview(offset: 8)
            $0.height(36)
            $0.width(36)
            $0.centerYToSuperview()
        }
        
        calendarAddIcon.run {
            $0.trailingToSuperview(offset: 10)
            $0.centerYToSuperview()
        }
    
        nameLabel.run {
            $0.topToSuperview(offset: 8)
            $0.leadingToTrailing(of: logoView, offset: 8)
            $0.trailingToSuperview(offset: 48)  
        }
        
        timeLabel.run {
            $0.topToBottom(of: nameLabel, offset: 4)
            $0.leadingToTrailing(of: logoView, offset: 8)
        }
    }

    func bind(_ contestItem: ContestItem, completion: @escaping (() -> ())) {
        nameLabel.text = contestItem.name
        timeLabel.text = contestItem.startTime
        onCalendarTap = completion
        logoView.image = contestItem.icon
    }
}
