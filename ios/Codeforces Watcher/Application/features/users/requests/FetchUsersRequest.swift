//
//  FetchUsers.swift
//  Codeforces Watcher
//
//  Created by Den Matyash on 1/6/20.
//  Copyright © 2020 xorum.io. All rights reserved.
//

import Foundation
import Alamofire

struct FetchUsersRequest {
    
    func execute(handles: String, completion: @escaping ([User]) -> ()) {
        let request = Alamofire.request("\(codeforcesApiLink)user.info?handles=\(handles)", parameters: ["lang": "en"])

        request.validate().responseString { response in
            switch response.result {
            case .success:
                if let json = response.result.value, let usersResponse = UsersResponse(JSONString: json)?.users {
                    print("Success fetching users")
                    DispatchQueue.main.async { completion(usersResponse) }
                } else {
                    print("Error")
                }
            case .failure:
                print("Error")
            }
        }
    }
    
    static func buildHandlesString(actions: [CFAction]) -> String {
        var handlesArray: [String] = []

        for action in actions {
            if let handle = action.comment?.commentatorHandle {
                handlesArray.append(handle)
            } else {
                handlesArray.append(action.blogEntry.authorHandle!)
            }
        }
        
        return handlesArray.joined(separator: ";")
    }
}
