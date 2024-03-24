import SwiftUI
import CommonSwiftUi
import Feed
import NewsfeedMultiplatform

/**
 Entry point of the app.
 */
@main
struct NewsfeedApp: App {
    init() {
        DependencyInjector.shared.initialise()
    }
    
    var body: some Scene {
        WindowGroup {
            ThemeView()
        }
    }
}

/**
 Applies the correct app theme based on the device's configuration.
 */
private struct ThemeView: View {
    @Environment(\.colorScheme) var colorScheme
    
    var body: some View {
        AppLaunchScreen().environmentObject(colorScheme == .light ? Theme(type: .light) : Theme(type: .dark))
    }
}

/**
 Provides the launch screen of the app.
 */
private struct AppLaunchScreen: View {
    var body: some View {
        FeedScreen()
    }
}
