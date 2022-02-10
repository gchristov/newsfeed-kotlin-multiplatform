import SwiftUI
import KmmShared
import Post
import CommonSwiftUi

public struct FeedScreen: View {
    public init() {
        // No-op
    }
    
    public var body: some View {
        FeedScreenContent(viewModel: FeedModule.shared.injectFeedViewModel())
    }
}

public struct FeedScreenContent: View {
    @State private var state: FeedViewModel.State?
    @State private var selectedPostId: String?
    private let viewModel: FeedViewModel
    
    public init(viewModel: FeedViewModel) {
        self.viewModel = viewModel
    }
    
    public var body: some View {
        ZStack {
            if state?.loading == true {
                LoadingState()
            } else if (state?.blockingError != nil) {
                ErrorState(blockingError: BlockingError()) {
                    viewModel.refreshContent()
                }
            } else {
                FeedState(
                    selectedPostId: $selectedPostId,
                    loading: state?.loading == true,
                    reachedEnd: state?.reachedEnd == true,
                    feed: state?.feed,
                    nonBlockingError: state?.nonBlockingError != nil ? NonBlockingError() : nil,
                    onNonBlockingErrorDismiss: { viewModel.dismissNonBlockingError() },
                    onRefresh: { viewModel.refreshContent() },
                    onLoadMore: { viewModel.loadNextPage(startFromFirst: false) },
                    onAppear: { viewModel.redecorateContent() }
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

private struct FeedState: View {
    @Binding var selectedPostId: String?
    let loading: Bool
    let reachedEnd: Bool
    let feed: Feed?
    let nonBlockingError: NonBlockingError?
    let onNonBlockingErrorDismiss: () -> ()
    let onRefresh: () -> ()
    let onLoadMore: () -> ()
    let onAppear: () -> ()
    
    var body: some View {
        AppNavigationView {
            AppScreen {
                ZStack {
                    if let feed = feed {
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
                        AppList(items: feed.posts) { decoratedPost, index in
                            let canLoadMore = index == feed.posts.count - 1
                            && !reachedEnd
                            && nonBlockingError == nil
                            PostRow(post: decoratedPost)
                            if (canLoadMore) {
                                LoadMoreRow().onAppear {
                                    onLoadMore()
                                }
                            }
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
            .onAppear {
                onAppear()
            }
            .navigationTitle("Newsfeed")
            .toolbar {
                ToolbarItemGroup(placement: .navigationBarTrailing) {
                    AppIconButton(
                        imageName: "arrow.clockwise",
                        contentDescription: "Reload"
                    ) {
                        onRefresh()
                    }
                }
            }
        }
        // Remove split style in landscape
        .navigationViewStyle(StackNavigationViewStyle())
    }
}

private struct PostRow: View {
    let post: DecoratedPost
    
    var body: some View {
        AppNavigationListRow {
            VStack(
                alignment: .leading,
                spacing: 8
            ) {
                PostHeader(post: post)
                if let body = post.post.body {
                    AppText(
                        text: body,
                        lineLimit: 3
                    ).padding(.top, 4)
                }
            }
        } destination: {
            PostScreen(postId: post.post.uid)
        }.modifier(RowPaddingStyle())
    }
}

private struct PostHeader: View {
    @EnvironmentObject var theme: Theme
    let post: DecoratedPost
    
    var body: some View {
        HStack(alignment: .top) {
            AppText(
                text: post.post.title,
                font: theme.typography.title
            )
            Spacer()
            if (post.isFavourite()) {
                AppIcon(
                    imageName: "heart.fill",
                    tint: theme.contentColors.action,
                    contentDescription: "Added to favourites"
                ).padding(.top, 2)
            }
        }
        PostAuthor(author: post.post.author)
    }
}

private struct PostAuthor: View {
    @EnvironmentObject var theme: Theme
    let author: String
    
    var body: some View {
        HStack(spacing: 6) {
            AppAvatar(
                name: author,
                size: CGSize(width: 24, height: 24)
            )
            AppText(
                text: author,
                color: theme.contentColors.secondary,
                font: theme.typography.subtitle
            )
        }
    }
}

private struct LoadMoreRow: View {
    var body: some View {
        AppListRow {
            HStack(alignment: .center) {
                Spacer()
                AppCircularProgressIndicator().padding(8)
                Spacer()
            }
        }.modifier(RowPaddingStyle())
    }
}

private struct RowPaddingStyle: ViewModifier {
    func body(content: Content) -> some View {
        content
            .padding(.leading, 16)
            .padding(.trailing, 16)
            .padding(.top, 8)
            .padding(.bottom, 8)
    }
}

private struct LoadingState: View {
    var body: some View {
        AppScreen {
            AppCircularProgressIndicator()
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

struct FeedView_Previews: PreviewProvider {
    static var post = DecoratedPost(
        post: Post(
            uid: "id",
            author: "Georgi",
            title: "Post title",
            body: "Some longer post body that can go on multiple lines and hopefull wrap around",
            pageId: nil,
            nextPageId: nil),
        favouriteTimestamp: nil)
    
    static var previews: some View {
        FeedScreen()
        //        PostRow(post: post)
        //        LoadMoreRow()
    }
}
