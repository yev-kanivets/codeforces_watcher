//
//  ProblemTableViewCell.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 1/17/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import UIKit

class ProblemTableViewCell: UITableViewCell {
    private let cardView = CardView()
    
    private let nameLabel = UILabel().apply {
        $0.font = Font.textHeading
        $0.textColor = Pallete.black
    }
    
    private let contestLabel = UILabel().apply {
        $0.font = Font.textSubheadingBig
        $0.textColor = Pallete.grey
    }
    
    private let starIcon = UIImageView(image: UIImage(named: "starIcon")).apply {
        $0.isHidden = true
    }

    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        setupView()
    }

    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        setupView()
    }

    private func setupView() {
        self.selectionStyle = .none
        
        buildViewTree()
        setConstraints()
    }

    private func buildViewTree() {
        contentView.addSubview(cardView)
        
        [nameLabel, contestLabel, starIcon].forEach(cardView.addSubview)
    }

    private func setConstraints() {
        cardView.edgesToSuperview(insets: UIEdgeInsets(top: 8, left: 8, bottom: 0, right: 8))
        
        starIcon.run {
            $0.trailingToSuperview(offset: 10)
            $0.centerYToSuperview()
        }
    
        nameLabel.run {
            $0.topToSuperview(offset: 8)
            $0.leadingToSuperview(offset: 8)
            $0.trailingToSuperview(offset: 48)
        }
        
        contestLabel.run {
            $0.topToBottom(of: nameLabel, offset: 4)
            $0.leadingToSuperview(offset: 8)
            $0.trailingToSuperview(offset: 48)
        }
    }

    func bind(_ problemItem: ProblemItem) {
        nameLabel.text = problemItem.title
        contestLabel.text = problemItem.round
    }
}
