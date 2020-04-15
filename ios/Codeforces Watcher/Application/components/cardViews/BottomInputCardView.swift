//
//  BottomInputCardView.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 4/15/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import Foundation
import UIKit
import MaterialComponents.MDCButton

class BottomInputCardView: UIView, UITextFieldDelegate {
    private let whiteView = CardView()
    
    private let explanationLabel = SubheadingBigLabel().apply {
        $0.text = "Add user".localized
    }
    
    lazy var textField = CommonTextField().apply {
        $0.delegate = self
        $0.placeholder = "Enter handle".localized
    }
    
    private let button = MDCButton().apply {
        $0.backgroundColor = Palette.colorPrimary
        $0.setTitle("Add".localized.uppercased(), for: .normal)
    }
    
    var shouldMoveFurther: (() -> ())?
    
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
        setInteractions()
    }
    
    private func buildViewTree() {
        addSubview(whiteView)
        
        [explanationLabel, textField, button].forEach(whiteView.addSubview)
    }
    
    private func setConstraints() {
        whiteView.edgesToSuperview()

        explanationLabel.run {
            $0.topToSuperview(offset: 16)
            $0.leadingToSuperview(offset: 16)
        }
        
        textField.run {
            $0.leadingToSuperview(offset: 16)
            $0.trailingToSuperview(offset: 16)
            $0.topToBottom(of: explanationLabel, offset: 16)
        }
        
        button.run {
            $0.trailingToSuperview(offset: 16)
            $0.bottomToSuperview(offset: -16)
        }
    }
    
    private func setInteractions() {
        button.addTarget(self, action: #selector(buttonTapped), for: .touchUpInside)
    }
    
    @objc func buttonTapped() {
        shouldMoveFurther?()
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        shouldMoveFurther?()
        return true
    }
}
