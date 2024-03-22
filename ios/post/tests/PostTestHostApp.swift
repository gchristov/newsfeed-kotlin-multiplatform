import SwiftUI
import CommonSwiftUi
import Post
import KmmShared

/*
 App to setup and run the UI tests for this module using a launch environment
 */
@main
struct PostTestHostApp: App {
    private let post: DecoratedPost
    private let postCache: DecoratedPost?
    private let repository: FakePostRepository
    private let decoratePostUseCase: DecoratePostUseCase
    
    init() {
        DependencyInjector.shared.initialise()
        // Mock/fake necessary constructs based on launch environment
        self.post = PostType.obtainFromEnvironment()
        self.postCache = PostCacheType.obtainFromEnvironment()
        self.repository = FakePostRepository(
            post: post,
            postCache: postCache
        )
        self.repository.postResponse = PostResponseType.obtainFromEnvironment()
        self.decoratePostUseCase = DecoratePostUseCase(postRepository: self.repository, dispatcher: Dispatchers.shared.Main)
    }
    
    var body: some Scene {
        WindowGroup {
            CustomSwiftUiTestRuleWrapper<PostScreenContent>(embedWithinNavigation: true) {
                PostScreenContent(viewModel: PostViewModel(
                    dispatcher: Dispatchers.shared.Main,
                    postId: PostId,
                    postRepository: repository,
                    decoratePostUseCase: decoratePostUseCase)
                )
            }
        }
    }
}

/*
 Obtains a test post from the launch environment to use during tests
 */
private enum PostType: String {
    case notFavourite = "notFavourite"
    case favourite = "favourite"
    
    static func obtainFromEnvironment() -> DecoratedPost {
        let type = PostType.init(rawValue: ProcessInfo.processInfo.environment["post"]!)
        switch type {
        case .favourite:
            return PostCreator.shared.post(favouriteTimestamp: 123)
        default:
            return PostCreator.shared.post(favouriteTimestamp: nil)
        }
    }
}

/*
 Obtains a test cache from the launch environment to use during tests
 */
private enum PostCacheType: String {
    case notFavourite = "notFavourite"
    
    static func obtainFromEnvironment() -> DecoratedPost? {
        if let cacheType = ProcessInfo.processInfo.environment["postCache"] {
            let type = PostType.init(rawValue: cacheType)
            switch type {
            case .notFavourite:
                return PostCreator.shared.post(favouriteTimestamp: nil)
            default:
                return nil
            }
        }
        return nil
    }
}

/*
 Obtains a test response type from the launch environment to use during tests
 */
private enum PostResponseType: String {
    case error = "error"
    case success = "success"
    case loadForever = "loadForever"
    
    static func obtainFromEnvironment() -> FakeResponse {
        let type = PostResponseType.init(rawValue: ProcessInfo.processInfo.environment["postResponse"]!)
        switch type {
        case .error:
            return FakeResponse.Error(message: nil)
        case .loadForever:
            return FakeResponse.LoadsForever()
        default:
            return FakeResponse.CompletesNormally()
        }
    }
}

private let PostId = "post_123"
