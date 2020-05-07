//
//  FeedbackCardView.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 5/6/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import Foundation
import UIKit
import StoreKit

class FeedbackCardView: UIView {
    private var feedbackItem: FeedbackItem!
    var callback: () -> () = {}

    private let cardView = CardView()
    private let titleLabel = HeadingLabel().apply {
        $0.textAlignment = .center
        $0.textColor = Palette.colorPrimary
    }
    private let closeButton = UIButton().apply {
        $0.setImage(UIImage(named: "crossIcon")?.withRenderingMode(.alwaysTemplate), for: .normal)
        $0.tintColor = Palette.grey
    }
    private let negativeButton = SmallButton().apply {
        $0.mode = .bordered
    }
    private let positiveButton = SmallButton()
    
    public override init(frame: CGRect) {
        super.init(frame: frame)
        setupView()
    }

    public required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        setupView()
    }

    private func setupView() {
        backgroundColor = Palette.white

        buildViewTree()
        setInteractions()
    }

    private func buildViewTree() {
        addSubview(cardView)

        let stackView = UIStackView().apply {
            $0.axis = .horizontal
            $0.spacing = 16
            $0.alignment = .center
            $0.distribution = .fillEqually
        }
        [negativeButton, positiveButton].forEach(stackView.addArrangedSubview)
        [titleLabel, closeButton, stackView].forEach(cardView.contentView.addSubview)

        cardView.run {
            let insets = UIEdgeInsets(top: 4, left: 8, bottom: 4, right: 8)
            $0.edgesToSuperview(insets: insets)
        }

        closeButton.run {
            $0.width(20)
            $0.height(20)
            $0.trailingToSuperview(offset: 4)
            $0.topToSuperview(offset: 4)
        }

        titleLabel.run {
            $0.height(50)
            $0.topToBottom(of: closeButton)
            $0.leadingToSuperview(offset: 16)
            $0.trailingToSuperview(offset: 16)
        }

        stackView.run {
            $0.leadingToSuperview(offset: 32)
            $0.topToBottom(of: titleLabel, offset: 16)
            $0.bottomToSuperview(offset: -16)
            $0.trailingToSuperview(offset: 32)
        }
    }

    private func setInteractions() {
        closeButton.addTarget(self, action: #selector(closeButtonTapped), for: .touchUpInside)
        negativeButton.addTarget(self, action: #selector(negativeButtonTapped), for: .touchUpInside)
        positiveButton.addTarget(self, action: #selector(positiveButtonTapped), for: .touchUpInside)
    }

    @objc private func closeButtonTapped() {
        feedbackItem.neutralButtonClick()
        callback()
    }

    @objc private func negativeButtonTapped() {
        feedbackItem.negativeButtonClick()
        callback()
    }

    @objc private func positiveButtonTapped() {
        feedbackItem.positiveButtonClick()
        callback()
    }

    public func bind() {
        feedbackItem = feedbackController.feedbackItem

        titleLabel.text = feedbackItem.textTitle
        positiveButton.setTitle(feedbackItem.textPositiveButton, for: .normal)
        negativeButton.setTitle(feedbackItem.textNegativeButton, for: .normal)
    }
}
