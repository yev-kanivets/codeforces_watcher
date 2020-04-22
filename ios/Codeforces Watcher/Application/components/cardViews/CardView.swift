//
//  CardView.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 1/3/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import Foundation
import UIKit
import TinyConstraints

class CardView: UIView {

    private let shadowView = UIView().apply {
        $0.layer.masksToBounds = false
        $0.backgroundColor = .clear
        $0.layer.run {
            $0.shadowColor = UIColor.black.cgColor
            $0.shadowOffset = CGSize(width: 0.0, height: 0.0)
            $0.shadowOpacity = 0.1
            $0.shadowRadius = 7.0
        }
    }

    let contentView = UIView().apply {
        $0.backgroundColor = .white
        $0.layer.run {
            $0.masksToBounds = true
            $0.cornerRadius = 4
        }
    }

    public override init(frame: CGRect) {
        super.init(frame: frame)
        setupView()
    }

    public required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        setupView()
    }

    private func setupView() {
        clipsToBounds = false
        buildViewTree()
        setConstraints()
    }

    private func buildViewTree() {
        addSubview(shadowView)
        shadowView.addSubview(contentView)
    }

    private func setConstraints() {
        shadowView.edgesToSuperview()
        contentView.edgesToSuperview()
    }
}
