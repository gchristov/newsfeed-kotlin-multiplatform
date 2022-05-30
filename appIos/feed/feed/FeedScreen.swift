import SwiftUI
import KmmShared
import Post
import CommonSwiftUi

/**
 This is the actual screen that is pushed on the navigation stack when app starts
 */
public struct FeedScreen: View {
    public init() {
        // No-op
    }
    
    public var body: some View {
        FeedScreenContent(viewModel: FeedModule.shared.injectFeedViewModel())
    }
}

/**
 This is a wrapper view for the different Feed States: ErrorState and FeedState. It also holds and sets up the FeedViewModel.
 All interactions with ViewModel are implemented here, but called on each specific state via callbacks.
 */
public struct FeedScreenContent: View {
    @State private var state: FeedViewModel.State?
    @State private var selectedPostId: String?
    private let viewModel: FeedViewModel
    
    public init(viewModel: FeedViewModel) {
        self.viewModel = viewModel
    }
    
    public var body: some View {
        ZStack {
            if (state?.blockingError != nil) {
                ErrorState(blockingError: BlockingError()) {
                    viewModel.refreshContent()
                }
            } else {
                FeedState(
                    selectedPostId: $selectedPostId,
                    feedItemDateFormatter: FeedItemDateFormatter,
                    feedSectionDateFormatter: FeedSectionDateFormat,
                    loading: state?.loading == true,
                    reachedEnd: state?.reachedEnd == true,
                    feed: state?.sectionedFeed,
                    nonBlockingError: state?.nonBlockingError != nil ? NonBlockingError() : nil,
                    onNonBlockingErrorDismiss: { viewModel.dismissNonBlockingError() },
                    onRefresh: { viewModel.refreshContent() },
                    onLoadMore: { viewModel.loadNextPage(startFromFirst: false) },
                    onAppear: { viewModel.redecorateContent() },
                    onSearchChanged: { query in viewModel.onSearchTextChanged(newQuery: query) }
                )
            }
        }
        .onAppear {
            setupViewModel()
        }
        .onDisappear {
            viewModel.onCleared()
        }
        .onDeepLink(deepLink: "post") { queryItems in
            selectedPostId = queryItems?.first(where: { $0.name == "postId" })?.value
        }
        .onDeepLink(deepLink: "home") { _ in
            selectedPostId = nil
        }
    }
    
    private func setupViewModel() {
        viewModel.state.addObserver { (newState) in
            state = newState
        }
    }
}

/**
 This is the 'normal' state the Feed view can be into
 */
private struct FeedState: View {
    @EnvironmentObject var theme: Theme
    @Binding var selectedPostId: String?
    @State private var searchQuery = ""
    let feedItemDateFormatter: DateFormatter
    let feedSectionDateFormatter: DateFormatter
    let loading: Bool
    let reachedEnd: Bool
    let feed: SectionedFeed?
    let nonBlockingError: NonBlockingError?
    let onNonBlockingErrorDismiss: () -> ()
    let onRefresh: () -> ()
    let onLoadMore: () -> ()
    let onAppear: () -> ()
    let onSearchChanged: (String) -> ()
    
    var body: some View {
        AppNavigationView {
            AppScreen {
                ZStack {
                    if let feed = feed {
                        if (!loading && feed.sections.isEmpty) {
                            FeedEmpty()
                        } else {
                            FeedContent(
                                selectedPostId: $selectedPostId,
                                feedItemDateFormatter: FeedItemDateFormatter,
                                feedSectionDateFormatter: FeedSectionDateFormat,
                                reachedEnd: reachedEnd,
                                feed: feed,
                                nonBlockingError: nonBlockingError,
                                onLoadMore: onLoadMore
                            )
                        }
                    }
                    if nonBlockingError != nil {
                        AppNonBlockingErrorView(
                            nonBlockingError: NonBlockingError(),
                            onRetry: onLoadMore,
                            onDismiss: onNonBlockingErrorDismiss
                        )
                    }
                }
            }
            .onChange(of: searchQuery) { _ in
                onSearchChanged(searchQuery)
            }
            .searchable(text: $searchQuery, prompt: "Search \(searchQuery.lowercased())")
            .onAppear {
                onAppear()
            }
            .navigationTitle("Newsfeed")
            .toolbar {
                ToolbarItemGroup(placement: .navigationBarTrailing) {
                    if (loading) {
                        AppCircularProgressIndicator(tint: theme.contentColors.primary)
                    } else {
                        AppIconButton(
                            imageName: "arrow.clockwise",
                            contentDescription: "Reload"
                        ) {
                            onRefresh()
                        }
                    }
                }
            }
        }
        // Remove split style in landscape
        .navigationViewStyle(StackNavigationViewStyle())
    }
}

private struct FeedEmpty: View {
    @EnvironmentObject var theme: Theme
    
    var body: some View {
        AppText(
            text: "No results found. Please\ntry another search term",
            color: theme.contentColors.secondary.opacity(0.6),
            textAlignment: .center
        )
    }
}

private struct FeedContent: View {
    @Binding var selectedPostId: String?
    let feedItemDateFormatter: DateFormatter
    let feedSectionDateFormatter: DateFormatter
    let reachedEnd: Bool
    let feed: SectionedFeed
    let nonBlockingError: NonBlockingError?
    let onLoadMore: () -> ()
    
    var body: some View {
        if (selectedPostId != nil) {
            // Fake navigation link to handle programmatic selection of detail views
            NavigationLink(
                destination: PostScreen(postId: selectedPostId!),
                tag: selectedPostId!,
                selection: self.$selectedPostId
            ) {
                EmptyView()
            }
        }
        AppGroupedList { scope in
            ForEach(Array(feed.sections.enumerated()), id: \.offset) { index, section in
                scope.group(header: section.type.toHeader(headerFormatter: feedSectionDateFormatter)) { groupScope in
                    groupScope.items(items: section.feedItems) { index in
                        FeedItemRow(
                            item: section.feedItems[index],
                            dateFormatter: feedItemDateFormatter
                        )
                    }
                }
                if (
                    index == feed.sections.count - 1
                    && !reachedEnd
                    && nonBlockingError == nil
                ) {
                    scope.item {
                        LoadMoreRow().onAppear {
                            onLoadMore()
                        }
                    }
                }
            }
        }
    }
}

private struct FeedItemRow: View {
    @EnvironmentObject var theme: Theme
    let item: DecoratedFeedItem
    let dateFormatter: DateFormatter
    
    var body: some View {
        AppNavigationListRow {
            HStack(
                alignment: .top,
                spacing: 16
            ) {
                FeedItemThumbnail(thumbnailUrl: item.raw.thumbnail)
                VStack(
                    alignment: .leading,
                    spacing: 4
                ) {
                    FeedItemHeader(header: item.raw.headline ?? "--")
                    FeedItemDate(
                        timestamp: item.date.toEpochMilliseconds()/1000,
                        dateFormatter: dateFormatter
                    )
                }
                Spacer() // Ensure the content is pushed to the left, rather than centered
                if (item.isFavourite()) {
                    AppIcon(
                        imageName: "heart.fill",
                        tint: theme.contentColors.action,
                        contentDescription: "Added to favourites"
                    ).padding(.top, 2)
                }
            }
        } destination: {
            PostScreen(postId: item.raw.itemId)
        }
    }
}

private struct FeedItemThumbnail: View {
    @EnvironmentObject var theme: Theme
    let thumbnailUrl: String?
    
    var body: some View {
        // Placeholder container
        ZStack {
            if let thumbnailUrl = thumbnailUrl {
                AppImage(
                    imageUrl: thumbnailUrl,
                    contentMode: .fill
                )
            }
        }
        .frame(width: 46, height: 46)
        // This is within a surface so invert the background
        .background(theme.backgrounds.primary)
        .clipShape(Circle())
    }
}

private struct FeedItemHeader: View {
    @EnvironmentObject var theme: Theme
    let header: String
    
    var body: some View {
        AppText(
            text: header,
            font: theme.typography.bodyBold
        )
    }
}

private struct FeedItemDate: View {
    @EnvironmentObject var theme: Theme
    let timestamp: Int64
    let dateFormatter: DateFormatter
    
    var body: some View {
        AppText(
            text: dateFormatter.string(from: Date(timeIntervalSince1970: Double(timestamp))),
            color: theme.contentColors.secondary,
            font: theme.typography.subtitle
        )
    }
}

private struct LoadMoreRow: View {
    var body: some View {
        AppListRow {
            HStack {
                Spacer()
                AppCircularProgressIndicator()
                Spacer()
            }
        }
    }
}

private struct ErrorState: View {
    let blockingError: BlockingError
    let onRetry: () -> ()
    
    var body: some View {
        AppScreen {
            AppBlockingErrorView(
                blockingError: blockingError,
                onRetry: onRetry
            )
        }
    }
}

private extension SectionedFeed.SectionType {
    func toHeader(headerFormatter: DateFormatter) -> String? {
        if self is SectionedFeed.SectionTypeThisWeek { return "This week" }
        if self is SectionedFeed.SectionTypeLastWeek { return "Last week" }
        if self is SectionedFeed.SectionTypeThisMonth { return "This month" }
        if let date = (self as? SectionedFeed.SectionTypeOlder)?.date.toEpochMilliseconds() {
            return headerFormatter.string(from: Date(timeIntervalSince1970: (Double(date) / 1000.0)))
        }
        return nil
    }
}

private extension DecoratedFeedItem {
    func isFavourite() -> Bool {
        return favouriteTimestamp != nil
    }
}

private var FeedItemDateFormatter: DateFormatter = {
    let formatter = DateFormatter()
    formatter.dateFormat = "dd/MM/yyyy"
    return formatter
}()
private var FeedSectionDateFormat: DateFormatter = {
    let formatter = DateFormatter()
    formatter.dateFormat = "MMM, yyyy"
    return formatter
}()
