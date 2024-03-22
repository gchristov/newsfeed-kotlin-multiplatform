import SwiftUI
import CommonSwiftUi

public struct CustomSwiftUiTestRuleWrapper<Content>: View where Content: View {
    let embedWithinNavigation: Bool
    let content: () -> Content
    
    public init(
        embedWithinNavigation: Bool = false,
        @ViewBuilder content: @escaping () -> Content
    ) {
        self.embedWithinNavigation = embedWithinNavigation
        self.content = content
    }
    
    public var body: some View {
        let theme = Theme(type: .dark)
        if embedWithinNavigation {
            NavigationView {
                content().environmentObject(theme)
            }
        } else {
            content().environmentObject(theme)
        }
    }
}
