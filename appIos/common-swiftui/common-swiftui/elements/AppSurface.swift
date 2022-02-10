import SwiftUI

public struct AppSurface<Content>: View where Content: View {
    @EnvironmentObject var theme: Theme
    private let content: () -> Content
    
    public init(@ViewBuilder content: @escaping () -> Content) {
        self.content = content
    }
    
    public var body: some View {
        ZStack {
            content()
        }
        .padding(16)
        .background(theme.backgrounds.surface)
        .cornerRadius(10)
        .shadow(radius: 2, x: 0.2, y: 0.5)
    }
}
