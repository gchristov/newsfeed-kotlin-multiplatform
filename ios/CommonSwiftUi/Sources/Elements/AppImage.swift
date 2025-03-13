import SwiftUI
import SDWebImageSwiftUI

public struct AppImage: View {
    @EnvironmentObject var theme: Theme
    private let imageUrl: String
    private let contentMode: ContentMode
    private let contentDescription: String?
    
    public init(
        imageUrl: String,
        contentMode: ContentMode = .fit,
        contentDescription: String? = nil
    ) {
        self.imageUrl = imageUrl
        self.contentMode = contentMode
        self.contentDescription = contentDescription
    }
    
    public var body: some View {
        if let contentDescription = contentDescription {
            WebImage(url: URL(string: imageUrl))
                .resizable()
                .aspectRatio(contentMode: contentMode)
                .accessibilityLabel(contentDescription)
        } else {
            WebImage(url: URL(string: imageUrl))
                .resizable()
                .aspectRatio(contentMode: contentMode)
        }
    }
}
