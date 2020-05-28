//
//  ContestsRulesCardView.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 1/27/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import Foundation
import UIKit

class ContestsRulesCardView: UIView {
    
    private let cardView = CardView()

    private let titleLabel = HeadingLabel().apply {
        $0.text = "Official Codeforces rules".localized
    }

    private let subtitleLabel = SubheadingBigLabel().apply {
        $0.text = "Apple isn't a sponsor of any contests conducted on Codeforces".localized
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
        buildViewTree()
        setConstraints()
    }

    private func buildViewTree() {
        self.addSubview(cardView)

        [titleLabel, subtitleLabel].forEach(cardView.addSubview)
    }

    private func setConstraints() {
        cardView.edgesToSuperview(insets: UIEdgeInsets(top: 4, left: 8, bottom: 4, right: 8))

        titleLabel.run {
            $0.topToSuperview(offset: 8)
            $0.leadingToSuperview(offset: 8)
            $0.trailingToSuperview(offset: 48)
        }

        subtitleLabel.run {
            $0.topToBottom(of: titleLabel, offset: 4)
            $0.leadingToSuperview(offset: 8)
            $0.trailingToSuperview(offset: 8)
            $0.bottomToSuperview(offset: -8)
        }
    }
}
