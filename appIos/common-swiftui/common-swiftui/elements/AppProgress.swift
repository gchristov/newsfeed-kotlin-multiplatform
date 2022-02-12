import SwiftUI

public struct AppCircularProgressIndicator: View {
    @EnvironmentObject var theme: Theme
    private let tint: Color?
    
    public init(tint: Color? = nil) {
        self.tint = tint
    }
    
    public var body: some View {
        ProgressView()
            .progressViewStyle(CircularProgressViewStyle(tint: tint ?? theme.contentColors.action))
            .accessibilityLabel("Loading")
    }
}
