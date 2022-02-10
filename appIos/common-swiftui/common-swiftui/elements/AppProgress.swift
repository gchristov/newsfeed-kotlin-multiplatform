import SwiftUI

public struct AppCircularProgressIndicator: View {
    @EnvironmentObject var theme: Theme
    
    public init() {
        // No-op
    }
    
    public var body: some View {
        ProgressView()
            .progressViewStyle(CircularProgressViewStyle(tint: theme.contentColors.action))
            .accessibilityLabel("Loading")
    }
}
