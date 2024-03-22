import SwiftUI

extension URL {
    var isDeeplink: Bool {
        return scheme == "news"
    }
    
    var queryItems: [URLQueryItem]? {
        guard let url = URLComponents(string: self.absoluteString) else { return nil }
        return url.queryItems
    }
}

public extension View {
    func onDeepLink(
        deepLink: String,
        onDeepLink: @escaping ([URLQueryItem]?) -> ()
    ) -> some View {
        return self.onOpenURL { url in
            guard url.isDeeplink else { return }
            
            if (url.host == deepLink) {
                onDeepLink(url.queryItems)
            }
        }
    }
}
