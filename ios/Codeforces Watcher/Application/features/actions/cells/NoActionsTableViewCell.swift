//
//  NoActionsView.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 1/14/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import Foundation
import UIKit
import TinyConstraints

class NoActionsTableViewCell: UITableViewCell {
    
    private let noActionsImageView = UIImageView(image: UIImage(named: "noItemsImage"))

<<<<<<< HEAD
    private let noActionsLabel = UILabel().apply {
        $0.font = Font.textHeading
        $0.textColor = Palette.black
=======
    private let noActionsLabel = HeadingLabel().apply {
>>>>>>> #116. Implement pinned post.
        $0.text = "Recent Actions are on the way to your device...".localized
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

    func buildViewTree() {
        [noActionsImageView, noActionsLabel].forEach(self.addSubview)
    }

    func setConstraints() {
        noActionsImageView.run {
            $0.centerXToSuperview()
            $0.centerYToSuperview(offset: -(16 + noActionsLabel.frame.height))
        }

        noActionsLabel.run {
            $0.topToBottom(of: noActionsImageView, offset: 16)
            $0.textAlignment = .center
            $0.leadingToSuperview(offset: 16)
            $0.trailingToSuperview(offset: 16)
        }
    }
}
