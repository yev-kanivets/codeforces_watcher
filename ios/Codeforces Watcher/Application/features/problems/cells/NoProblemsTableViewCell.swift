//
//  NoProblemsTableViewCell.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 1/22/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import Foundation
import UIKit
import TinyConstraints

class NoProblemsTableViewCell: UITableViewCell {
    
    private let noProblemsImageView = UIImageView(image: UIImage(named: "noItemsImage"))

    private let noProblemsLabel = UILabel().apply {
        $0.font = Font.textHeading
        $0.textColor = Palette.black
        $0.numberOfLines = 0
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
        [noProblemsImageView, noProblemsLabel].forEach(self.addSubview)
    }

    func setConstraints() {
        noProblemsImageView.run {
            $0.centerXToSuperview()
            $0.centerYToSuperview(offset: -(16 + noProblemsLabel.frame.height))
        }

        noProblemsLabel.run {
            $0.topToBottom(of: noProblemsImageView, offset: 16)
            $0.textAlignment = .center
            $0.leadingToSuperview(offset: 16)
            $0.trailingToSuperview(offset: 16)
        }
    }
    
    func bind(_ text: String) {
        noProblemsLabel.text = text.localized
    }
}
