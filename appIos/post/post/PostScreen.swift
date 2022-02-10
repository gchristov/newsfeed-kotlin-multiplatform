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
            if state?.loading == true {
                LoadingState()
            } else if (state?.blockingError != nil) {
                ErrorState(blockingError: BlockingError()) {
                    viewModel.loadContent()
                }
            } else if let post = state?.post {
                PostState(post: post)
            }
        }
        .onAppear {
            setupViewModel()
        }
        .onDisappear {
            viewModel.onCleared()
        }
        .navigationBarTitle("", displayMode: .inline)
        .toolbar {
            ToolbarItemGroup(placement: .navigationBarTrailing) {
                AppIconButton(
                    imageName: "heart\(state?.post?.isFavourite() ?? false ? ".fill" : "")",
                    contentDescription: state?.post?.isFavourite() ?? false ? "Remove from favourites" : "Add to favourites"
                ) {
                    viewModel.onToggleFavourite()
                }
                AppIconButton(
                    imageName: "arrow.clockwise",
                    contentDescription: "Reload"
                ) {
                    viewModel.loadContent()
                }
            }
        }
    }
    
    private func setupViewModel() {
        viewModel.state.addObserver { (newState) in
            state = newState
        }
    }
}

private struct PostState: View {
    let post: DecoratedPost
    
    var body: some View {
        AppScreen {
            ScrollView {
                Spacer(minLength: 16)
                VStack(
                    alignment: .leading,
                    spacing: 8
                ) {
                    PostHeader(post: post)
                    if let body = post.post.body {
                        AppText(text: body).padding(.top)
                    }
                }.padding(16)
            }
        }
    }
}

private struct PostHeader: View {
    @EnvironmentObject var theme: Theme
    let post: DecoratedPost
    
    var body: some View {
        AppText(
            text: post.post.title,
            font: theme.typography.title
        )
        PostAuthor(author: post.post.author)
    }
}

private struct PostAuthor: View {
    @EnvironmentObject var theme: Theme
    let author: String
    
    var body: some View {
        HStack(spacing: 8) {
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

struct PostDetailsView_Previews: PreviewProvider {
    private static var post = DecoratedPost(
        post: Post(
            uid: "id",
            author: "Georgi",
            title: "Post title",
            body: "Some longer post body that can go on multiple lines",
            pageId: nil,
            nextPageId: nil),
        favouriteTimestamp: nil)
    
    static var previews: some View {
        PostState(post: post)
    }
}
