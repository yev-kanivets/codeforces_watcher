//
//  PinnedPostTableViewCell.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 4/13/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import UIKit

class PinnedPostTableViewCell: UITableViewCell {
    private let cardView = CardView()
    private let infoImage = CircleImageView().apply {
        $0.image = UIImage(named: "infoImage")?.withRenderingMode(.alwaysTemplate)
        $0.tintColor = Pallete.colorPrimary
    }
    
    private let headingLabel = HeadingLabel().apply {
        $0.textColor = Pallete.blue
    }
    
    private let subheadingLabel = SubheadingLabel().apply {
        $0.text = "Click to see details".localized
    }
    
    private let crossImage = UIImageView(image: UIImage(named: "crossIcon")?.withRenderingMode(.alwaysTemplate)).apply {
        $0.tintColor = Pallete.grey
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
        selectionStyle = .none

        buildViewTree()
        setConstraints()
    }

    private func buildViewTree() {
        contentView.addSubview(cardView)

        [infoImage, headingLabel, subheadingLabel, crossImage].forEach(cardView.addSubview)
    }
    
    private func setConstraints() {
        cardView.edgesToSuperview(insets: UIEdgeInsets(top: 8, left: 8, bottom: 0, right: 8))
        
        infoImage.run {
            $0.leadingToSuperview(offset: 8)
            $0.topToSuperview(offset: 8)
            $0.height(36)
            $0.width(36)
        }
        
        headingLabel.run {
            $0.topToSuperview(offset: 8)
            $0.trailingToLeading(of: crossImage)
            $0.leadingToTrailing(of: infoImage, offset: 8)
        }
        
        subheadingLabel.run {
            $0.leadingToTrailing(of: infoImage, offset: 8)
            $0.trailingToSuperview(offset: 8)
            $0.topToBottom(of: headingLabel, offset: 4)
        }
        
        crossImage.run {
            $0.trailingToSuperview(offset: 8)
            $0.topToSuperview(offset: 8)
        }
    }
    
    func bind(title: String, link: String) {
        headingLabel.text = title
        
    }
}
