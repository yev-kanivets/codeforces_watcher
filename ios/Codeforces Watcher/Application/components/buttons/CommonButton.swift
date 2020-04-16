//
//  CommonButton.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 4/16/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import UIKit
import TinyConstraints

class CommonButton: UIButton {
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupView()
    }
    
    private func setupView() {
        titleLabel?.font = Font.textButton
        contentEdgeInsets = UIEdgeInsets(top: 10, left: 25, bottom: 10, right: 25)
        layer.run {
            $0.cornerRadius = 18
            $0.masksToBounds = true
        }
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
}
