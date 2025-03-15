import SwiftUI

public struct AppScreen<Content>: View where Content: View {
    @EnvironmentObject var theme: Theme
    private let content: () -> Content
    
    public init(@ViewBuilder content: @escaping () -> Content) {
        self.content = content
    }
    
    public var body: some View {
        ZStack {
            content()
        }
        .frame(
            minWidth: 0,
            maxWidth: .infinity,
            minHeight: 0,
            maxHeight: .infinity
        )
        .background(theme.backgrounds.primary.ignoresSafeArea()) // Safe are includes navigation view
    }
}
