import SwiftUI

public struct AppGroupedList<Content>: View where Content: View {
    private let showDividers: Bool
    private let content: (AppGroupedListScope) -> Content
    
    public init(
        showDividers: Bool = true,
        @ViewBuilder content: @escaping (AppGroupedListScope) -> Content
    ) {
        self.showDividers = showDividers
        self.content = content
        // Remove list dividers from UIKit views
        UITableView.appearance().separatorColor = .clear
    }
    
    public var body: some View {
        List {
            content(AppGroupedListScope(showDividers: showDividers))
        }
        .modifier(AppListStyle())
    }
}

public class AppGroupedListScope {
    private let showDividers: Bool
    private var groupCount = 0
    
    init(showDividers: Bool) {
        self.showDividers = showDividers
    }
    
    public func group<Content: View>(
        header: String? = nil,
        @ViewBuilder content: (AppGroupScope) -> Content
    ) -> some View {
        let isFirstGroup = groupCount == 0
        let horizontalPadding = AppGroupedListSpacing * 2
        var topPadding = AppGroupedListSpacing / 2
        if (!isFirstGroup) {
            topPadding += AppGroupedListSpacing
        }
        groupCount += 1
        
        return Group {
            AppGroupHeader(text: header).padding(EdgeInsets(top: topPadding, leading: horizontalPadding, bottom: 12, trailing: horizontalPadding))
            content(AppGroupScope(showDividers: showDividers))
        }
    }
    
    public func item<Content: View>(@ViewBuilder content: () -> Content) -> some View {
        content()
    }
}

public class AppGroupScope {
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
        return AppGroupItem(
            showDivider: showDividers,
            itemIndex: itemIndex,
            itemCount: itemCount,
            content: content
        )
    }
}

private struct AppGroupHeader: View {
    @EnvironmentObject var theme: Theme
    private let text: String?
    
    init(text: String?) {
        self.text = text
    }
    
    public var body: some View {
        ZStack {
            if let text = text {
                AppText(
                    text: text.uppercased(),
                    color: theme.contentColors.secondary,
                    font: theme.typography.subtitle
                )
            }
        }
        .modifier(AppListItemStyle())
    }
}

private struct AppGroupItem<Content>: View where Content: View {
    @EnvironmentObject var theme: Theme
    private let showDivider: Bool
    private let itemIndex: Int
    private let itemCount: Int
    private let isAtLastItem: Bool
    private let content: (Int) -> Content
    
    init(
        showDivider: Bool,
        itemIndex: Int,
        itemCount: Int,
        @ViewBuilder content: @escaping (Int) -> Content
    ) {
        self.showDivider = showDivider
        self.itemIndex = itemIndex
        self.itemCount = itemCount
        self.content = content
        self.isAtLastItem = itemIndex == itemCount - 1
    }
    
    private func shapeForPosition(
        itemIndex: Int,
        itemCount: Int,
        isAtLastItem: Bool
    ) -> some Shape {
        if itemCount == 1 {
            return theme.shapes.groupSingle
        } else if itemIndex == 0 {
            return theme.shapes.groupStart
        } else if isAtLastItem {
            return theme.shapes.groupEnd
        }
        return theme.shapes.groupMiddle
    }
    
    public var body: some View {
        VStack(spacing: 0) {
            content(itemIndex)
            if (showDivider && !isAtLastItem) {
                Rectangle()
                    .fill(theme.backgrounds.primary)
                    .frame(height: 1)
            }
        }
            .background(theme.backgrounds.surface)
            .clipShape(shapeForPosition(
                itemIndex: itemIndex,
                itemCount: itemCount,
                isAtLastItem: isAtLastItem
            ))
            .padding(.horizontal, AppGroupedListSpacing)
            .modifier(AppListItemStyle())
    }
}

private let AppGroupedListSpacing = 16.0
