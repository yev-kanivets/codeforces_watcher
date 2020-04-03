//
//  ActionsCell.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 1/8/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import UIKit
import SDWebImage
import common

class BlogEntryTableViewCell: UITableViewCell {
    private let cardView = CardView()
    
    private let blogEntryTitleLabel = UILabel().apply {
        $0.font = Font.textHeading
        $0.textColor = Pallete.black
    }
    
    private let userImage = UIImageView().apply {
        $0.layer.run {
            $0.cornerRadius = 18
            $0.masksToBounds = true
            $0.borderWidth = 1
            $0.borderColor = Pallete.colorPrimary.cgColor
        }
    }
    
    private let userHandleLabel = UILabel().apply {
        $0.textColor = Pallete.green
        $0.font = Font.textSubheading
    }
    
    private let someTimeAgoLabel = UILabel().apply {
        $0.textColor = Pallete.grey
        $0.font = Font.textSubheading
    }
    
    private let detailsLabel = UILabel().apply {
        $0.numberOfLines = 1
        $0.textColor = Pallete.grey
        $0.font = Font.textBody
        
        $0.text = "Created or updated the text, click to see details..."
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
        
        [blogEntryTitleLabel, userImage, userHandleLabel, someTimeAgoLabel, detailsLabel].forEach(cardView.addSubview)
    }

    private func setConstraints() {
        cardView.edgesToSuperview(insets: UIEdgeInsets(top: 8, left: 8, bottom: 0, right: 8))

        userImage.run {
            $0.leadingToSuperview(offset: 8)
            $0.topToSuperview(offset: 8)
            $0.height(36)
            $0.width(36)
        }
        
        blogEntryTitleLabel.run {
            $0.topToSuperview(offset: 8)
            $0.trailingToSuperview(offset: 8)
            $0.leadingToTrailing(of: userImage, offset: 8)
        }
        
        userHandleLabel.run {
            $0.leadingToTrailing(of: userImage, offset: 8)
            $0.trailingToLeading(of: someTimeAgoLabel)
            $0.topToBottom(of: blogEntryTitleLabel, offset: 4)
            $0.setContentHuggingPriority(.defaultHigh, for: .horizontal)
        }
        
        someTimeAgoLabel.run {
            $0.topToBottom(of: blogEntryTitleLabel, offset: 4)
            $0.leadingToTrailing(of: userHandleLabel)
            $0.trailingToSuperview(offset: 8)
        }
        
        detailsLabel.run {
            $0.topToBottom(of: userImage, offset: 14)
            $0.leadingToSuperview(offset: 8)
            $0.trailingToSuperview(offset: 8)
            $0.bottomToSuperview(offset: -8)
        }
    }

    func bind(_ action: CFAction) {
        let blogEntry = action.blogEntry
        guard let timePassed = TimeInterval((Int(Date().timeIntervalSince1970) - Int(action.timeSeconds))).socialDate else { return }
        
        blogEntryTitleLabel.text = blogEntry.title
        userHandleLabel.attributedText = colorTextByUserRank(text: blogEntry.authorHandle, rank: blogEntry.authorRank)
        someTimeAgoLabel.text = " - \(timePassed) " + "ago".localized
        
        detailsLabel.text = blogEntry.content
        
        if let avatar = blogEntry.authorAvatar {
            userImage.sd_setImage(with: URL(string: avatar), placeholderImage: noImage)
        } else {
            userImage.image = noImage
        }
    }
}
