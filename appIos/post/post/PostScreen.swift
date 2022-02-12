import SwiftUI
import KmmShared
import CommonSwiftUi

public struct PostScreen: View {
    private let postId: String
    
    public init(postId: String) {
        self.postId = postId
    }
    
    public var body: some View {
        PostScreenContent(viewModel: PostModule.shared.injectPostViewModel(postId: postId))
    }
}

public struct PostScreenContent: View {
    @State private var state: PostViewModel.State?
    private let viewModel: PostViewModel
    
    public init(viewModel: PostViewModel) {
        self.viewModel = viewModel
    }
    
    public var body: some View {
        ZStack {
            if (state?.blockingError != nil) {
                ErrorState(blockingError: BlockingError()) {
                    viewModel.loadContent()
                }
            } else {
                PostState(
                    loading: state?.loading == true,
                    post: state?.post,
                    onRefresh: { viewModel.loadContent() },
                    onToggleFavourite: { viewModel.onToggleFavourite() }
                )
            }
        }
        .onAppear {
            setupViewModel()
        }
        .onDisappear {
            viewModel.onCleared()
        }
    }
    
    private func setupViewModel() {
        viewModel.state.addObserver { (newState) in
            state = newState
        }
    }
}

private struct PostState: View {
    @EnvironmentObject var theme: Theme
    let loading: Bool
    let post: DecoratedPost?
    let onRefresh: () -> ()
    let onToggleFavourite: () -> ()
    
    var body: some View {
        AppScreen {
            ScrollView {
                if let post = post {
                    VStack {
                        PostImage(url: post.raw.thumbnail)
                        VStack(
                            alignment: .leading,
                            spacing: 8
                        ) {
                            PostHeader(header: post.raw.headline ?? "--")
                            PostBody(text: post.raw.body ?? "--")
                        }.padding(16)
                    }
                }
            }
        }
        .ignoresSafeArea(edges: .top) // Allow nav bar to overlay content
        .navigationBarTitle("", displayMode: .inline)
        .toolbar {
            ToolbarItemGroup(placement: .navigationBarTrailing) {
                PostNavigationActions(
                    loading: loading,
                    allowFavourite: post != nil,
                    isFavourite: post?.isFavourite() == true,
                    onRefresh: onRefresh,
                    onToggleFavourite: onToggleFavourite
                )
            }
        }
    }
}

private struct PostNavigationActions: View {
    @EnvironmentObject var theme: Theme
    let loading: Bool
    let allowFavourite: Bool
    let isFavourite: Bool
    let onRefresh: () -> ()
    let onToggleFavourite: () -> ()
    
    var body: some View {
        if (allowFavourite) {
            AppIconButton(
                imageName: "heart\(isFavourite ? ".fill" : "")",
                contentDescription: isFavourite ? "Remove from favourites" : "Add to favourites"
            ) {
                onToggleFavourite()
            }
        }
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

private struct PostImage: View {
    @EnvironmentObject var theme: Theme
    let url: String?
    
    var body: some View {
        ZStack(alignment: .top) {
            if let url = url {
                AppImage(
                    imageUrl: url,
                    contentMode: .fill
                )
            }
            Rectangle()
                .fill(LinearGradient(gradient: Gradient(colors: [theme.backgrounds.surface.opacity(0.7), .black.opacity(0)]), startPoint: .top, endPoint: .bottom))
                .frame(height: 200)
        }
        .frame(
            // Fill max available width
            minWidth: 0,
            maxWidth: .infinity,
            minHeight: 340,
            maxHeight: 340
        )
        .background(theme.backgrounds.surface)
        .clipped()
    }
}

private struct PostHeader: View {
    @EnvironmentObject var theme: Theme
    let header: String
    
    var body: some View {
        AppText(
            text: header,
            font: theme.typography.title
        )
        PostAuthor(author: "Anonymous")
    }
}

private struct PostAuthor: View {
    @EnvironmentObject var theme: Theme
    let author: String
    
    var body: some View {
        HStack(spacing: 6) {
            AppAvatar(
                name: author,
                size: CGSize(width: 36, height: 36)
            )
            VStack(alignment: .leading) {
                AppText(
                    text: author,
                    font: theme.typography.subtitle
                )
                AppText(
                    text: "1 min read",
                    color: theme.contentColors.secondary,
                    font: theme.typography.caption
                )
            }
            Spacer()
        }
    }
}

private struct PostBody: View {
    @EnvironmentObject var theme: Theme
    let text: String
    
    var body: some View {
        AppText(text: text).padding(.top, 16)
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

private extension DecoratedPost {
    func isFavourite() -> Bool {
        return favouriteTimestamp != nil
    }
}
