//
//  ActionsCell.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 1/8/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import UIKit
import common

class CommentTableViewCell: UITableViewCell {

    private let cardView = CardView()

<<<<<<< HEAD
    private let blogEntryTitleLabel = UILabel().apply {
        $0.font = Font.textHeading
        $0.textColor = Palette.blue
    }

    private let userImage = UIImageView().apply {
        $0.layer.run {
            $0.cornerRadius = 18
            $0.masksToBounds = true
            $0.borderWidth = 1
            $0.borderColor = Palette.colorPrimary.cgColor
        }
    }
=======
    private let blogEntryTitleLabel = HeadingLabel().apply {
        $0.textColor = Pallete.blue
        $0.numberOfLines = 1
    }

    private let userImage = CircleImageView()
>>>>>>> #116. Implement pinned post.

    private let commentedByLabel = SubheadingLabel().apply {
        $0.text = "Commented by".localized + " "
<<<<<<< HEAD
        $0.textColor = Palette.grey
        $0.font = Font.textSubheading
    }

    private let userHandleLabel = UILabel().apply {
        $0.textColor = Palette.green
        $0.font = Font.textSubheading
    }

    private let someTimeAgoLabel = UILabel().apply {
        $0.textColor = Palette.grey
        $0.font = Font.textSubheading
    }
=======
    }

    private let userHandleLabel = SubheadingLabel()

    private let someTimeAgoLabel = SubheadingLabel()
>>>>>>> #116. Implement pinned post.

    private let detailsLabel = BodyLabel().apply {
        $0.numberOfLines = 3
<<<<<<< HEAD
        $0.textColor = Palette.grey
        $0.font = Font.textBody
=======
>>>>>>> #116. Implement pinned post.
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

        [blogEntryTitleLabel, userImage, commentedByLabel, userHandleLabel, someTimeAgoLabel, detailsLabel].forEach(cardView.addSubview)
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

        commentedByLabel.run {
            $0.leadingToTrailing(of: userImage, offset: 8)
            $0.topToBottom(of: blogEntryTitleLabel, offset: 4)
            $0.setContentHuggingPriority(.defaultHigh, for: .horizontal)
        }

        userHandleLabel.run {
            $0.leadingToTrailing(of: commentedByLabel)
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
        guard let comment = action.comment else { return }
        guard let timePassed = TimeInterval((Int(Date().timeIntervalSince1970) - Int(action.timeSeconds))).socialDate else { return }

        blogEntryTitleLabel.text = blogEntry.title.beautify()
        userHandleLabel.attributedText = colorTextByUserRank(text: comment.commentatorHandle, rank: comment.commentatorRank)

        someTimeAgoLabel.text = " - \(timePassed) " + "ago".localized

        detailsLabel.text = comment.text.beautify()

        if var avatar = comment.commentatorAvatar {
            avatar = LinkValidatorKt.avatar(avatarLink: avatar)
            userImage.sd_setImage(with: URL(string: avatar), placeholderImage: noImage)
        } else {
            userImage.image = noImage
        }
    }
}
