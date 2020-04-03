//
//  ActionItem.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 1/8/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import Foundation
import HTMLString
/*
enum ActionItem {
    struct CommentItem {
        var shareText: String
        var link: String
        var commentatorHandle: NSMutableAttributedString
        var blogTitle: String
        var content: String
        var time: String
        var commentatorAvatar: String
        
        init(action: CFAction, rank: String?, avatar: String) {
            self.blogTitle = action.blogEntry.title!.beautify()
            self.time = TimeInterval((Int(Date().timeIntervalSince1970) - Int(action.timeSeconds!))).socialDate ?? ""
            self.content = action.comment!.text!.beautify()
            self.commentatorHandle = colorTextByUserRank(text: action.comment!.commentatorHandle!, rank: rank)
            self.commentatorAvatar = "https:" + avatar
            self.link = "\(codeforcesLink)blog/entry/\(action.blogEntry.id!)?#comment-\(action.comment!.id!)"
            self.shareText = """
            \(blogTitle) - \(link)
            
            Shared through Codeforces Watcher. Find it on App Store.
            """
        }
    }

    struct BlogEntryItem {
        var shareText: String
        var link: String
        var authorHandle: NSMutableAttributedString
        var blogTitle: String
        var time: String
        var authorAvatar: String
        
        init(action: CFAction, rank: String?, avatar: String) {
            self.blogTitle = action.blogEntry.title!.beautify()
            self.time = TimeInterval((Int(Date().timeIntervalSince1970) - Int(action.timeSeconds!))).socialDate ?? ""
            self.authorHandle = colorTextByUserRank(text: action.blogEntry!.authorHandle!, rank: rank)
            self.authorAvatar = "https:" + avatar
            self.link = "\(codeforcesLink)blog/entry/\(action.blogEntry.id!)"
            self.shareText = """
            \(blogTitle) - \(link)
            
            Shared through Codeforces Watcher. Find it on App Store.
            """
        }
    }
    
    case comment(CommentItem)
    case blogEntry(BlogEntryItem)
}
*/
