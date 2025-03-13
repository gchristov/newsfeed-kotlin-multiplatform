import SwiftUI

public struct AppIcon: View {
    @EnvironmentObject var theme: Theme
    private let imageName: String
    private let tint: Color?
    private let contentDescription: String?
    
    public init(
        imageName: String,
        tint: Color? = nil,
        contentDescription: String? = nil
    ) {
        self.imageName = imageName
        self.tint = tint
        self.contentDescription = contentDescription
    }
    
    public var body: some View {
        if let contentDescription = contentDescription {
            Image(systemName: imageName)
                .foregroundColor(tint ?? theme.contentColors.primary)
                .accessibilityLabel(contentDescription)
        } else {
            Image(systemName: imageName).foregroundColor(tint ?? theme.contentColors.primary)
        }
    }
}
