//
//  NoContestsTableViewCell.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 1/23/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import Foundation
import UIKit
import TinyConstraints

class NoContestsTableViewCell: UITableViewCell {
    private let noContestsImageView = UIImageView(image: UIImage(named: "noItemsImage"))
    
    private let noContestsLabel = UILabel().apply {
        $0.font = Font.textHeading
        $0.textColor = Pallete.black
        $0.text = "Contests are on the way to your device...".localized
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
        [noContestsImageView, noContestsLabel].forEach(self.addSubview)
    }
    
    func setConstraints() {
        noContestsImageView.run {
            $0.centerXToSuperview()
            $0.centerYToSuperview(offset: -(16 + noContestsLabel.frame.height))
        }
        
        noContestsLabel.run {
            $0.topToBottom(of: noContestsImageView, offset: 16)
            $0.textAlignment = .center
            $0.leadingToSuperview(offset: 16)
            $0.trailingToSuperview(offset: 16)
        }
    }
}
