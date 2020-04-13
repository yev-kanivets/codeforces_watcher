//
//  FilterTableViewCell.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 1/28/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import Foundation
import UIKit
import common

class FilterTableViewCell: UITableViewCell {
    
<<<<<<< HEAD
    private var logoView = UIImageView().apply {
        $0.layer.run {
            $0.cornerRadius = 20
            $0.masksToBounds = true
            $0.borderWidth = 1
            $0.borderColor = Palette.colorPrimary.cgColor
        }
    }

    private let nameLabel = UILabel().apply {
        $0.font = Font.textHeading
        $0.textColor = Palette.black
    }
=======
    private var logoView = CircleImageView()

    private let nameLabel = HeadingLabel()
>>>>>>> #116. Implement pinned post.

    private let switchView = UISwitch().apply {
        $0.onTintColor = Palette.colorPrimary
    }

    private var platform: Platform!

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
        setInteractions()
    }

    @objc func switchTrigger(mySwitch: UISwitch) {
        store.dispatch(action: ContestsRequests.ChangeFilterCheckStatus(platform: platform, isChecked: switchView.isOn))
    }

    private func buildViewTree() {
        [logoView, nameLabel, switchView].forEach(self.addSubview)
    }

    private func setConstraints() {
        logoView.run {
            $0.leadingToSuperview(offset: 16)
            $0.height(40)
            $0.width(40)
            $0.centerYToSuperview()
        }

        nameLabel.run {
            $0.centerYToSuperview()
            $0.leadingToTrailing(of: logoView, offset: 8)
        }

        switchView.run {
            $0.centerYToSuperview()
            $0.trailingToSuperview(offset: 16)
        }
    }

    private func setInteractions() {
        switchView.addTarget(self, action: #selector(switchTrigger), for: UIControl.Event.valueChanged)
    }

    func bind(_ filterItem: FilterItem) {
        logoView.image = UIImage(named: Platform.getImageNameByPlatform(filterItem.platform))
        nameLabel.text = filterItem.title
        switchView.isOn = filterItem.isOn
        platform = filterItem.platform
    }
}
