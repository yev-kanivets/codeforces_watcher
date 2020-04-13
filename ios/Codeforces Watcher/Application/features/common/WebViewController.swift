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

class WebViewController: UIViewController, WKUIDelegate, WKNavigationDelegate {

    private lazy var webView = WKWebView(frame: .zero, configuration: WKWebViewConfiguration()).apply {
        $0.uiDelegate = self
        $0.allowsBackForwardNavigationGestures = true
        $0.navigationDelegate = self
    }

    var link: String!
    var shareText: String!

    var openEventName: String! = ""
    var shareEventName: String! = ""

    override func viewDidLoad() {
        PKHUD.sharedHUD.contentView = PKHUDProgressView()
        PKHUD.sharedHUD.userInteractionOnUnderlyingViewsEnabled = true
        PKHUD.sharedHUD.show()

        onLoadViewLogEvent()

        super.viewDidLoad()
        view = webView
        navigationItem.rightBarButtonItem = UIBarButtonItem(image: UIImage(named: "shareImage"), style: .plain, target: self, action: #selector(shareTapped))
        openWebPage()
    }

    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        PKHUD.sharedHUD.hide(afterDelay: 0)
    }

    @objc func shareTapped() {
        let activityController = UIActivityViewController(activityItems: [shareText!], applicationActivities: nil).apply {
            $0.popoverPresentationController?.barButtonItem = navigationItem.rightBarButtonItem
        }

        present(activityController, animated: true, completion: nil)

        onShareViewLogEvent()
    }

    func onLoadViewLogEvent() {
        guard !openEventName.isEmpty else { return }
        Analytics.logEvent(openEventName, parameters: [:])
    }

    func onShareViewLogEvent() {
        guard !shareEventName.isEmpty else { return }
        Analytics.logEvent(shareEventName, parameters: [:])
    }

    func openWebPage() {
        if let url = URL(string: link) {
            webView.load(URLRequest(url: url))
        }
    }

    func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
        PKHUD.sharedHUD.hide(afterDelay: 0)
    }

    func webView(_ webView: WKWebView, didFail navigation: WKNavigation!, withError error: Error) {
        PKHUD.sharedHUD.hide(afterDelay: 0)
    }
}
