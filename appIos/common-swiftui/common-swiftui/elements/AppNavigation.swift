import SwiftUI
import Introspect

public struct AppNavigationView<Content>: View where Content: View {
    @EnvironmentObject var theme: Theme
    private let content: () -> Content
    
    public init(@ViewBuilder content: @escaping () -> Content) {
        self.content = content
    }
    
    public var body: some View {
        let color = theme.contentColors.primary
        NavigationView {
            // Find the parent navigation controller and set its appearance to support theme changes
            content().introspectNavigationController { navigationController in
                navigationController.navigationBar.standardAppearance = UINavigationBarAppearance.appStandardAppearance(theme: theme)
                navigationController.navigationBar.compactAppearance = UINavigationBarAppearance.appCompactAppearance(theme: theme)
                navigationController.navigationBar.scrollEdgeAppearance = UINavigationBarAppearance.appScrollingEdgeAppearance(theme: theme)
            }
        }
        .accentColor(color)
        // Initially set the navigation bar theme to avoid flickering when it appears
        .modifier(NavigationBarModifier(theme: theme))
    }
}

private struct NavigationBarModifier: ViewModifier {
    init(theme: Theme) {
        UINavigationBar.appearance().standardAppearance = UINavigationBarAppearance.appStandardAppearance(theme: theme)
        UINavigationBar.appearance().compactAppearance = UINavigationBarAppearance.appCompactAppearance(theme: theme)
        UINavigationBar.appearance().scrollEdgeAppearance = UINavigationBarAppearance.appScrollingEdgeAppearance(theme: theme)
    }
    
    func body(content: Content) -> some View {
        content
    }
}

private extension UINavigationBarAppearance {
    static func appStandardAppearance(theme: Theme) -> UINavigationBarAppearance {
        return appCollapsedAppearance(theme: theme)
    }
    
    static func appCompactAppearance(theme: Theme) -> UINavigationBarAppearance {
        return appCollapsedAppearance(theme: theme)
    }
    
    static func appScrollingEdgeAppearance(theme: Theme) -> UINavigationBarAppearance {
        return appExpandedAppearance(theme: theme)
    }
    
    private static func appCollapsedAppearance(theme: Theme) -> UINavigationBarAppearance {
        let appearance = baseAppearance(theme: theme)
        appearance.backgroundEffect = UIBlurEffect(style: .systemUltraThinMaterial)
        appearance.backgroundColor = UIColor(theme.backgrounds.surface.opacity(0.2))
        return appearance
    }
    
    private static func appExpandedAppearance(theme: Theme) -> UINavigationBarAppearance {
        let appearance = baseAppearance(theme: theme)
        appearance.configureWithTransparentBackground()
        appearance.shadowColor = .clear
        return appearance
    }
    
    private static func baseAppearance(theme: Theme) -> UINavigationBarAppearance {
        let appearance = UINavigationBarAppearance()
        appearance.titleTextAttributes = [.foregroundColor: UIColor(theme.contentColors.primary)]
        appearance.largeTitleTextAttributes = [.foregroundColor: UIColor(theme.contentColors.primary)]
        return appearance
    }
}
