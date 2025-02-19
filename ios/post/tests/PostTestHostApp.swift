import SwiftUI
import CommonSwiftUi
import Post
import NewsfeedMultiplatform

/*
 App to setup and run the UI tests for this module using a launch environment
 */
@main
struct PostTestHostApp: App {
    private let post: Post
    private let repository: FakePostRepository
    
    init() {
        DependencyInjector.shared.initialise()
        // Mock/fake necessary constructs based on launch environment
        self.post = PostCreator.shared.post(
            id: "post_123",
            title: "Post Title",
            body: "This is a sample post body",
            date: "2022-02-21T00:00:00Z"
        )
        self.repository = FakePostRepository(
            post: post,
            usePostForCache: UsePostForCacheType.obtainFromEnvironment(),
            readingTimeMinutes: 1
        )
        self.repository.postResponse = PostResponseType.obtainFromEnvironment()
    }
    
    var body: some Scene {
        WindowGroup {
            CustomSwiftUiTestRuleWrapper<PostScreenContent>(embedWithinNavigation: true) {
                PostScreenContent(viewModel: PostViewModel(
                    dispatcher: Dispatchers.shared.Main,
                    postId: PostId,
                    postRepository: repository)
                )
            }
        }
    }
}

private enum UsePostForCacheType: String {
    case doNotUseForCache = "false"
    case useForCache = "true"
    
    static func obtainFromEnvironment() -> Bool {
        let type = UsePostForCacheType.init(rawValue: ProcessInfo.processInfo.environment["usePostForCache"]!)
        switch type {
        case .useForCache:
            return true
        default:
            return false
        }
    }
}

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
