//
//  WebViewController.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 1/29/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import Foundation
import WebKit
import FirebaseAnalytics
import PKHUD

class WebViewController: UIViewControllerWithCross, WKUIDelegate, WKNavigationDelegate {

    private lazy var webView = WKWebView(frame: .zero, configuration: WKWebViewConfiguration()).apply {
        $0.uiDelegate = self
        $0.allowsBackForwardNavigationGestures = true
        $0.navigationDelegate = self
    }

    var link: String!
    var shareText: String!

    var openEventName: String?
    var shareEventName: String?

    override func viewDidLoad() {
        super.viewDidLoad()
        view = webView
        navigationItem.rightBarButtonItem = UIBarButtonItem(image: UIImage(named: "shareImage"), style: .plain, target: self, action: #selector(shareTapped))
        openWebPage()
        showLoading()
        onLoadViewLogEvent()
    }
    
    private func showLoading() {
        PKHUD.sharedHUD.userInteractionOnUnderlyingViewsEnabled = true
        HUD.show(.progress, onView: UIApplication.shared.windows.last)
    }
    
    private func hideLoading() {
        HUD.hide(afterDelay: 0)
    }

    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        hideLoading()
    }

    @objc func shareTapped() {
        let activityController = UIActivityViewController(activityItems: [shareText!], applicationActivities: nil).apply {
            $0.popoverPresentationController?.barButtonItem = navigationItem.rightBarButtonItem
        }

        present(activityController, animated: true)

        onShareViewLogEvent()
    }

    private func onLoadViewLogEvent() {
        if let openEventName = openEventName {
            Analytics.logEvent(openEventName, parameters: [:])
        }
    }

    private func onShareViewLogEvent() {
        if let shareEventName = shareEventName {
            Analytics.logEvent(shareEventName, parameters: [:])
        }
    }

    private func openWebPage() {
        if let url = URL(string: link) {
            webView.load(URLRequest(url: url))
        }
    }

    func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
        hideLoading()
    }

    func webView(_ webView: WKWebView, didFail navigation: WKNavigation!, withError error: Error) {
        hideLoading()
    }
}
