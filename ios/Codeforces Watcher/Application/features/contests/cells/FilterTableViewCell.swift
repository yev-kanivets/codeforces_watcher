//
//  FilterTableViewCell.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 1/28/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import Foundation
import UIKit

class FilterTableViewCell: UITableViewCell {
    private var logoView = UIImageView().apply {
        $0.layer.run {
            $0.cornerRadius = 20
            $0.masksToBounds = true
            $0.borderWidth = 1
            $0.borderColor = Pallete.colorPrimary.cgColor
        }
    }
    
    private let nameLabel = UILabel().apply {
        $0.font = Font.textHeading
        $0.textColor = Pallete.black
    }
    
    private let switchView = UISwitch().apply {
        $0.onTintColor = Pallete.colorPrimary
    }
    
    private var onSwitchChanged: ((Bool) -> ())!
    
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
        
        switchView.addTarget(self, action: #selector(switchTrigger), for: UIControl.Event.valueChanged)
    }

    @objc func switchTrigger(mySwitch: UISwitch) {
        onSwitchChanged(switchView.isOn)
    }
    
    func buildViewTree() {
        [logoView, nameLabel, switchView].forEach(self.addSubview)
    }
    
    func setConstraints() {
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
    
    func bind(platform: Platform, completion: @escaping ((_ isOn: Bool) -> ())) {
        logoView.image = UIImage(named: platform.rawValue)
        nameLabel.text = platform.rawValue
        switchView.isOn = store.state.contests.filters[platform]!
        onSwitchChanged = completion
    }
}
