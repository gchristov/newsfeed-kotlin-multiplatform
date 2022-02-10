import SwiftUI

public struct AppList<Data, RowContent>: View where Data: AnyObject, RowContent: View {
    private let items: [Data]
    private let rowContent: (Data, Int) -> RowContent
    
    public init(
        items: [Data],
        @ViewBuilder rowContent: @escaping (Data, Int) -> RowContent
    ) {
        self.items = items
        self.rowContent = rowContent
        // Remove list dividers from UIKit views
        UITableView.appearance().separatorColor = .clear
    }
    
    public var body: some View {
        List(0 ..< items.count, id: \.self) { index in
            rowContent(items[index], index)
        }.modifier(AppListStyle())
    }
}

private struct AppListStyle: ViewModifier {
    func body(content: Content) -> some View {
        content
            .listStyle(PlainListStyle()) // Remove list padding
            .background(Color.clear.ignoresSafeArea()) // Clear color so that lists get the current screen color. Safe are includes navigation view.
    }
}

public struct AppListRow<Content>: View where Content: View {
    private let content: () -> Content
    
    public init(@ViewBuilder content: @escaping () -> Content) {
        self.content = content
    }
    
    public var body: some View {
        content().modifier(AppListRowStyle())
    }
}

public struct AppSurfaceListRow<Content>: View where Content: View {
    private let content: () -> Content
    
    public init(@ViewBuilder content: @escaping () -> Content) {
        self.content = content
    }
    
    public var body: some View {
        AppSurface(content: content).modifier(AppListRowStyle())
    }
}

public struct AppNavigationListRow<Content, Destination>: View where Content: View, Destination: View {
    private let content: () -> Content
    private let destination: () -> Destination
    
    public init(
        @ViewBuilder content: @escaping () -> Content,
        @ViewBuilder destination: @escaping () -> Destination
    ) {
        self.content = content
        self.destination = destination
    }
    
    public var body: some View {
        ZStack {
            AppSurface {
                content()
            }
            // Navigation link without chevron
            NavigationLink(destination: destination) {
                EmptyView()
            }
            .buttonStyle(PlainButtonStyle())
            .opacity(0)
        }.modifier(AppListRowStyle())
    }
}

private struct AppListRowStyle: ViewModifier {
    func body(content: Content) -> some View {
        content
            .listRowBackground(Color.clear) // Clear color so that list rows get the current screen color
            .listRowInsets(EdgeInsets())
    }
}
