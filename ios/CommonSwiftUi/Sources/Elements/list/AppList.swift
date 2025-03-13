import SwiftUI

public struct AppList<Content>: View where Content: View {
    private let showDividers: Bool
    private let content: (AppListScope) -> Content
    
    public init(
        showDividers: Bool = true,
        @ViewBuilder content: @escaping (AppListScope) -> Content
    ) {
        self.showDividers = showDividers
        self.content = content
        // Remove list dividers from UIKit views
        UITableView.appearance().separatorColor = .clear
    }
    
    public var body: some View {
        List {
            content(AppListScope(showDividers: showDividers))
        }.modifier(AppListStyle())
    }
}

public class AppListScope {
    private let showDividers: Bool
    private var itemCount = 0
    
    init(showDividers: Bool) {
        self.showDividers = showDividers
    }
    
    public func items<Data: AnyObject, Content: View>(
        items: [Data],
        @ViewBuilder itemContent: @escaping (Int) -> Content
    ) -> some View {
        let firstItemIndex = itemCount
        itemCount += items.count
        
        return ForEach(0 ..< items.count, id: \.self) { index in
            self.item(
                itemIndex: firstItemIndex + index,
                content: itemContent
            )
        }
    }
    
    private func item<Content: View>(
        itemIndex: Int,
        @ViewBuilder content: @escaping (Int) -> Content
    ) -> some View {
        let isAtLastRow = itemIndex == itemCount - 1
        
        return AppListItem(
            showDivider: showDividers && !isAtLastRow,
            itemIndex: itemIndex,
            content: content
        )
    }
}

private struct AppListItem<Content>: View where Content: View {
    @EnvironmentObject var theme: Theme
    private let showDivider: Bool
    private let itemIndex: Int
    private let content: (Int) -> Content
    
    init(
        showDivider: Bool,
        itemIndex: Int,
        @ViewBuilder content: @escaping (Int) -> Content
    ) {
        self.showDivider = showDivider
        self.itemIndex = itemIndex
        self.content = content
    }
    
    public var body: some View {
        VStack(spacing: 0) {
            content(itemIndex)
            if (showDivider) {
                Rectangle()
                    .fill(theme.backgrounds.primary)
                    .frame(height: 1)
            }
        }
            .background(theme.backgrounds.surface)
            .modifier(AppListItemStyle())
    }
}

struct AppListStyle: ViewModifier {
    func body(content: Content) -> some View {
        content
            .environment(\.defaultMinListRowHeight, 0)
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
        content()
            .modifier(AppListItemStyle())
            .padding(AppListSpacing)
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
            content()
            // Navigation link without chevron
            NavigationLink(destination: destination) {
                EmptyView()
            }
            .buttonStyle(PlainButtonStyle())
            .opacity(0)
        }
        .modifier(AppListItemStyle())
        .padding(AppListSpacing)
    }
}

struct AppListItemStyle: ViewModifier {
    func body(content: Content) -> some View {
        HStack {
            content
            Spacer() // Fill max available width
        }
            .listRowInsets(EdgeInsets())
            .listRowBackground(Color.clear) 
    }
}

private let AppListSpacing = 16.0
